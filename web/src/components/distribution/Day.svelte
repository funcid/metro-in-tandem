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
    import { findMetroStationById } from "../../utils/metro";

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
        endpoint: string = "day",
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
        let a = await response.json();
        for (let alloc of a[0].allocations) {
            alloc.application = a[0].applications.find(
                (i: { id: number }) => i.id == alloc.applicationId,
            );
            console.log("found by id " + alloc);
        }
        console.log(a);
        a[0].allocations.sort((a, b) => (a.from > b.from ? 1 : -1));
        return a;
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
            label: `${alloc.employee.rank} / ${alloc.employee.fio}`,
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
                        id: app.applicationId,
                        resourceId: alloc.employee.id,
                        label: (() => {
                            let diffMinutes = to.diff(from, "minutes");
                            // let duration = (to.minutes() - from.minutes()).toFixed(0);
                            // return Number(duration) >= -35 ? duration + "м" : " ";
                            // return to.minutes() + " " + from.minutes()
                            return diffMinutes > 35 ? diffMinutes + "м" : " ";
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
                .filter((app) => app.from != app.to)
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

            const lunchBreak = alloc.allocations.find(
                (app) => app.type == "LUNCH_BREAK",
            );

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
        allocations = await fetchAllocations("reallocate", "POST");
        mapAllocationsToOptions(allocations);
    }
</script>

<main class="flex flex-col gap-[40rem]">
    <p class="font-bold text-[40rem]">Расписание на день</p>
    <!-- {JSON.stringify(allocations)} -->
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
        </div>
        <hr />
        <div>
            <SvelteGantt {...options} bind:this={ganttInstance} />
        </div>
        <div>
            {#if allocations && allocations[0]}
                {#each allocations[0].allocations as a}
                    {#if a.from != a.to}
                        <div
                            class=""
                            style="margin: 6px; padding: 8px; border-radius: 16px; background-color: #f5f5f5;"
                        >
                            <div class="">
                                {a.from.split(" ")[1]} - {a.to.split(" ")[1]}
                            </div>

                            <div class="" style="color: gray; padding-bottom: 6px;">{a.type == "APPLICATION" ? "Сопровождение заявки #" + a.application.id : a.type == "TRAVEL" ? "Перемещение к следующей заявке" : "Обеденный перерыв"}</div>
                            {#if a.application}
                                <div class="flex">
                                    <div class="">
                                        <img
                                            src={findMetroStationById(
                                                a.application.idSt1,
                                            )?.iconUrl}
                                            width="20px"
                                        />
                                    </div>
                                    <div class="" style="padding-left: 4px;">
                                        {findMetroStationById(
                                            a.application.idSt1,
                                        )?.nameStation}
                                    </div>
                                    <div class="" style="padding: 0px 6px;">
                                        →
                                    </div>
                                    <div class="">
                                        <img
                                            src={findMetroStationById(
                                                a.application.idSt2,
                                            )?.iconUrl}
                                            width="20px"
                                        />
                                    </div>
                                    <div class="" style="padding-left: 4px;">
                                        {findMetroStationById(
                                            a.application.idSt2,
                                        )?.nameStation}
                                    </div>
                                </div>
                            {/if}
                            <!-- {} -->
                        </div>
                    {/if}
                {/each}
            {/if}
        </div>
    </div>
</main>
