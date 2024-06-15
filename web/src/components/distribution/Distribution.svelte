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
        // other fields as needed
    }

    let ganttInstance: SvelteGantt | null = null; // To hold the instance of SvelteGantt
    let applications: ApplicationResponse[] = [];

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

    async function fetchApplications(): Promise<ApplicationResponse[]> {
        const response = await fetch(PUBLIC_API_HOST + `api/v1/applications`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${$JWT}`,
                "Content-Type": "application/json",
            },
        });
        if (!response.ok) {
            throw new Error("Failed to fetch applications data");
        }
        return await response.json();
    }

    // Update or insert tasks
    function handleUpdateTasks(models: any[] /* replace with Array<Task> */) {
        ganttInstance.updateTasks(models);
    }

    // Update or insert rows
    function handleUpdateRows(models: any[] /* replace with Array<Row> */) {
        ganttInstance.updateRows(models);
    }

    function mapApplicationsToOptions(applications: ApplicationResponse[]) {
        options.rows = applications.map((app) => ({
            id: app.id,
            label: app.fullName,
        }));
        handleUpdateRows(options.rows);

        options.tasks = applications.map((app) => ({
            id: app.id,
            resourceId: app.id,
            label: ' ',
            from: moment(app.time3, "HH:mm:ss"),
            to: moment(app.time4, "HH:mm:ss"),
            classes: "orange text-black",
            buttonClasses: "text-black",
            amountDone: 100
        }));
        handleUpdateTasks(options.tasks);
    }

    onMount(async () => {
        try {
            applications = await fetchApplications();
            mapApplicationsToOptions(applications);
        } catch (error) {
            console.error("Error fetching applications:", error);
        }
    });
</script>

<div>
    <SvelteGantt {...options} bind:this={ganttInstance} />
</div>
