<script lang="ts" type="module">
    import { onMount } from "svelte";
    import { JWT } from "../../utils/auth";
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
    let time: string = "24.4.2024";

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

    async function fetchAllocations(
        endpoint: string = "allocations",
        method: string = "GET",
    ): Promise<Allocation[]> {
        const response = await fetch(
            `${PUBLIC_API_HOST}api/v1/${endpoint}?&from=${options.from}&to=${options.to}`,
            {
                method: method,
                headers: {
                    Authorization: `Bearer ${$JWT}`,
                    "Content-Type": "application/json",
                },
            },
        );
        if (!response.ok) {
            throw new Error("Failed to fetch allocations data");
        }
        return await response.json();
    }

    function parseTimeWork(timeWork: string, date: string) {
        const [startTime, endTime] = timeWork
            .split("-")
            .map((time) => moment(date + " " + time, "YYYY-MM-DD HH:mm"));
        if (endTime.isBefore(startTime)) {
            return {
                segments: [
                    {
                        start: options.from,
                        end: endTime.clone().add(moment.duration(1, "day")),
                    },
                    {
                        start: startTime.clone().add(moment.duration(1, "day")),
                        end: options.to,
                    },
                ],
            };
        } else {
            return {
                segments: [{ start: startTime, end: endTime }],
            };
        }
    }

    async function mapAllocationsToOptions(allocations: Allocation[]) {
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
            timeWork: alloc.employee.timeWork,
        }));
        ganttInstance.updateRows(options.rows);

        options.tasks = allocations.flatMap((alloc) => {
            const tasks = alloc.allocations
                .filter((app) => app.type == "APPLICATION")
                .map((app) => {
                    const from = moment(app.from, "DD.MM.YYYY HH:mm:ss");
                    const to = moment(app.to, "DD.MM.YYYY HH:mm:ss");

                    return {
                        id: app.application.id,
                        resourceId: alloc.employee.id,
                        label: (() => {
                            let duration = (to.minutes() - from.minutes()).toFixed(0);
                            return Number(duration) >= 35 ? duration + "м" : " ";
                        })(),
                        from: from,
                        to: to,
                        showButton: true,
                        enableDragging: false,
                        enableResize: false,
                        classes: "application-task",
                    };
                })
                .filter((task) => task !== null);

            const travel = alloc.allocations
                .filter((app) => app.type == "TRAVEL")
                .map((app) => {
                    const from = moment(app.from, "DD.MM.YYYY HH:mm:ss");
                    const to = moment(app.to, "DD.MM.YYYY HH:mm:ss");

                    return {
                        id: Math.random(),
                        resourceId: alloc.employee.id,
                        label: " ",
                        from: from,
                        to: to,
                        showButton: false,
                        enableDragging: false,
                        enableResize: false,
                        classes: "application-travel",
                    };
                })
                .filter((task) => task !== null);

            const lunchBreak = alloc.allocations.find((app) => app.type == "LUNCH_BREAK")

            const from = moment(lunchBreak?.from, "DD.MM.YYYY HH:mm:ss");
            const to = moment(lunchBreak?.to, "DD.MM.YYYY HH:mm:ss");
            
            const lunch = {
                id: Math.random(),
                resourceId: alloc.employee.id,
                label: "Обед",
                from: from,
                to: to,
                showButton: false,
                enableDragging: false,
                enableResize: false,
                classes: "application-lunch",
            };

            const { segments } = parseTimeWork(
                alloc.employee.timeWork,
                alloc.employee.date,
            );

            const workTasks = segments.map((segment, index) => ({
                id: Math.random(),
                resourceId: alloc.employee.id,
                label: " ",
                from: segment.start,
                to: segment.end,
                showButton: false,
                enableDragging: false,
                enableResize: false,
                classes: "application-work",
            }));

            return [...travel, lunch, ...workTasks, ...tasks];
        });

        ganttInstance.updateTasks(options.tasks);
    }

    onMount(async () => {
        ganttInstance.api.tasks.on.select((task: any[]) => {
            if (!task[0]) return;
            if (!task[0].model.classes.includes("application-task")) {
                return;
            }
            window.location.hash = `/applications/${task[0].model.id}`;
            window.scrollTo(0, 0);
        });
    });

    async function reallocate() {
        allocations = await fetchAllocations('reallocate', 'POST');
        mapAllocationsToOptions(allocations);
    }
</script>

<main class="flex flex-col gap-[40rem]">
    <p class="font-bold text-[40rem]">Распределение заявок</p>
    <div class="flex flex-col mb-[30rem]">
        <div class="flex justify-between">
            <Flatpickr
                options={{
                    dateFormat: "d.m.Y",
                    noCalendar: false,
                    time_24hr: true,
                }}
                bind:value={time}
                on:change={handleDateChange}
                class="flex w-fit text-gray-700 border border-gray-400 p-[20rem] rounded-[20rem] mb-[30rem]"
            />
            <button
                class="bg-[#D4212D] h-[82rem] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[20rem] items-center text-white"
                on:click={reallocate}
            >
                Перераспределить
            </button>
        </div>
        <hr />
        <div>
            <SvelteGantt {...options} bind:this={ganttInstance} />
        </div>
    </div>
</main>
