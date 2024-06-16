<script lang="ts" type="module">
    import { onMount } from "svelte";
    import { JWT } from "../login/Login.svelte";
    import { PUBLIC_API_HOST } from "$env/static/public";
    import {
        SvelteGantt,
        SvelteGanttTable,
        SvelteGanttDependencies,
        MomentSvelteGanttDateAdapter,
    } from "svelte-gantt/svelte";
    import moment from "moment";
    import Flatpickr from "svelte-flatpickr";
    import "flatpickr/dist/flatpickr.css";

    let ganttInstance: SvelteGantt | null = null;
    let allocations: Allocation[] = [];
    let time: string = "24.4.2024"

    let options = {
        rows: [] as { id: number; label: string; timeWork: string }[],
        tasks: [] as Task[],
        dependencies: [],
        from: Date.now(),
        to: Date.now() + 1000 * 60 * 60 * 24,
        headers: [
            { unit: "day", format: "MMMM Do" },
            { unit: "hour", format: "H:mm" },
        ],
        fitWidth: true,
        ganttTableModules: [SvelteGanttTable],
        ganttBodyModules: [SvelteGanttDependencies],
        dateAdapter: new MomentSvelteGanttDateAdapter(moment),
        tableWidth: 160,
        tableHeaders: [{ title: "Сотрудники", property: "label" }],
        rowHeight: 40,
    };

    async function handleDateChange(event: any) {
        const [selectedDates, dateStr] = event.detail;
        if (selectedDates.length > 0) {
            options.from = Number(selectedDates[0]) + 1000 * 60 * 60 * 5.5;
            options.to = Number(selectedDates[0]) + 1000 * 60 * 60 * 25;
            allocations = await fetchAllocations();
            mapAllocationsToOptions(allocations);
        }
    }

    async function fetchAllocations(): Promise<Allocation[]> {
        const response = await fetch(PUBLIC_API_HOST + `api/v1/allocations?&from=${options.from}&to=${options.to}`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${$JWT}`,
                "Content-Type": "application/json",
            },
            
        });
        if (!response.ok) {
            throw new Error("Failed to fetch allocations data");
        }
        return await response.json();
    }

    function mapAllocationsToOptions(allocations: Allocation[]) {
        if (!Array.isArray(allocations)) {
            console.error(
                "Expected an array of allocations, but got:",
                allocations,
            );
            return;
        }

        options.rows = allocations.map((alloc) => ({
            id: alloc.employee.id,
            label: `${alloc.employee.fio} | ${alloc.employee.rank}`,
            timeWork: alloc.employee.timeWork, // Adding timeWork to the rows
        }));
        ganttInstance.updateRows(options.rows);

        options.tasks = allocations.flatMap((alloc) => {
            const tasks = alloc.applications.map((app) => ({
                id: app.id,
                resourceId: alloc.employee.id,
                label: (() => {
                    let time = moment
                        .duration(app.time4)
                        .subtract(moment.duration(app.time3))
                        .asMinutes()
                        .toFixed(0)
                    return Number(time) >= 35 ? (time + "м") : " "
                })(),
                from: moment(app.datetime, "DD.MM.YYYY").add(moment.duration(app.time3)),
                to: moment(app.datetime, "DD.MM.YYYY").add(moment.duration(app.time4)),
                showButton: true, // Assuming you don't need buttons on tasks
                enableDragging: false,
                enableResize: false,
            }));

            return [...tasks];
        });
        ganttInstance.updateTasks(options.tasks);
    }

    onMount(async () => {
        try {
            allocations = await fetchAllocations();
            mapAllocationsToOptions(allocations);
        } catch (error) {
            console.error("Error fetching allocations:", error);
        }
        ganttInstance.api.tasks.on.select((task: any[]) => {
            window.location.hash = `/applications/${task[0].model.id}`;
            window.scrollTo(0, 0);
        });
    });
</script>

<main>
    <p class="font-bold text-[40rem] mb-[20rem]">Распределение заявок</p>
    <div class="flex flex-col gap-[20rem]">
        <Flatpickr
            options={{
                dateFormat: "d.m.Y",
                noCalendar: false,
                time_24hr: true,
            }}
            bind:value={time}
            on:change={handleDateChange}
            class="flex shadow appearance-none border w-fit text-gray-700 border border-gray-400 p-[20rem] rounded-[20rem]"
        />
        <hr />
        <div>
            <hr />
            <SvelteGantt {...options} bind:this={ganttInstance} />
        </div>
    </div>
</main>
