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

    interface Application {
        id: number;
        idPas: string;
        datetime: string;
        time3: string;
        time4: string;
    }

    interface Employee {
        id: number;
        fio: string;
        timeWork: string;
    }

    interface Allocation {
        employee: Employee;
        applications: Application[];
    }

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
        from: Date.now() - 1000 * 60 * 60 * 24 * 53,
        to: Date.now() - 1000 * 60 * 60 * 24 * 51.5,
        headers: [
            { unit: "day", format: "MMMM Do" },
            { unit: "hour", format: "H:mm" },
        ],
        fitWidth: true,
        ganttTableModules: [SvelteGanttTable],
        ganttBodyModules: [SvelteGanttDependencies],
        dateAdapter: new MomentSvelteGanttDateAdapter(moment),
        tableWidth: 170,
        tableHeaders: [{ title: "Сотрудники", property: "label" }],
        rowHeight: 40,
        rowPadding: 6,
        timeRanges: [
            {
                id: "1",
                from: moment("01:00", "HH:mm"),
                to: moment("5:30", "HH:mm"),
                resizable: false,
                label: "Закрыто на ночь",
            },
            {
                id: "2",
                from: moment("01:00", "HH:mm").add(1, "day"),
                to: moment("5:30", "HH:mm").add(1, "day"),
                resizable: false,
                label: "Закрыто на ночь",
            },
        ],
    };

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

    function handleUpdateTasks(models: any[]) {
        ganttInstance.updateTasks(models);
    }

    function handleUpdateRows(models: any[]) {
        ganttInstance.updateRows(models);
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
        handleUpdateRows(options.rows);

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

            // Добавляем обеденные перерывы
            const startWork = moment(alloc.employee.timeWork.split("-")[0], "HH:mm");
            const endWork = moment(alloc.employee.timeWork.split("-")[1], "HH:mm");
            const lunchStart = startWork.clone().add(3, "hours").add(30, "minutes");
            const lunchEnd = endWork.clone().subtract(1, "hour");

            const lunchTasks = [];
            let lunchTime = lunchStart.clone();
            while (lunchTime.isBefore(lunchEnd)) {
                lunchTasks.push({
                    id: `${alloc.employee.id}-lunch-${lunchTime.format("HH:mm")}`,
                    resourceId: alloc.employee.id,
                    label: "Обед",
                    from: lunchTime,
                    to: lunchTime.clone().add(1, "hour"),
                    classes: "green text-black",
                });
                lunchTime.add(4, "hours").add(30, "minutes");
            }

            return tasks.concat(lunchTasks);
        });
        handleUpdateTasks(options.tasks);
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
    <SvelteGantt {...options} bind:this={ganttInstance} />
</div>
