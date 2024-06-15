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

    let options = {
        rows: [] as { id: number; label: string }[],
        tasks: [] as {
            id: number;
            resourceId: number;
            label: string;
            from: moment.Moment;
            to: moment.Moment;
            classes: string;
        }[],
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
        tableWidth: 130,
        tableHeaders: [{ title: "Сотрудники", property: "label" }],
        rowHeight: 40,
        rowPadding: 6,
    };

    function handleDateChange(event: any) {
        const [selectedDates, dateStr] = event.detail;
        if (selectedDates.length > 0) {
            options.from = Number(selectedDates[0]);
            options.to = Number(selectedDates[0]) + 1000 * 60 * 60 * 24;
            //async () => {
            //    allocations = await fetchAllocations();
            //    mapAllocationsToOptions(allocations);
            //}
        }
    }

    async function fetchAllocations(): Promise<Allocation[]> {
        const response = await fetch(PUBLIC_API_HOST + `api/v1/allocations`, {
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
            console.error("Expected an array of allocations, but got:", allocations);
            return;
        }

        options.rows = allocations.map((alloc) => ({
            id: alloc.employee.id,
            label: alloc.employee.fio,
        }));
        ganttInstance.updateRows(options.rows);

        options.tasks = allocations.flatMap((alloc) => {
            const tasks = alloc.applications.map((app) => ({
                id: app.id,
                resourceId: alloc.employee.id,
                label: `Заявка ${app.idPas}`,
                from: moment(app.datetime, "DD.MM.YYYY HH:mm:ss").add(moment.duration(app.time3)),
                to: moment(app.datetime, "DD.MM.YYYY HH:mm:ss").add(moment.duration(app.time4)),
                classes: "orange text-black",
                buttonClasses: "text-black",
                amountDone: 100,
            }));
            return tasks;
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
    });
</script>

<div>
    <Flatpickr
        options={{
            dateFormat: "d.m.Y",
            noCalendar: false,
            time_24hr: true,
        }}
        on:change={handleDateChange}
        class="flex shadow appearance-none border rounded-[12rem] p-[12rem] w-full text-gray-700"
    />
    <SvelteGantt {...options} bind:this={ganttInstance} />
</div>
