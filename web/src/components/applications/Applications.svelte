<script lang="ts">
    import { createMetroStation } from "../../utils/metro";
    import { onMount } from "svelte";
    import { JWT } from "../login/Login.svelte";
    import { PUBLIC_API_HOST } from "$env/static/public";

    let applications: ApplicationResponse[] = [];
    let loading: boolean = true;
    let errorMessage: string = "";

    onMount(async () => {
        await fetchApplications();
    });

    const fetchApplications = async () => {
        loading = true;
        try {
            const response = await fetch(
                PUBLIC_API_HOST + `api/v1/applications`,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${$JWT}`,
                        "Content-Type": "application/json",
                    },
                },
            );
            if (!response.ok) {
                return;
            }
            applications = await response.json();
        } catch (err) {
            errorMessage =
                "Failed to load applications. Please try again later.";
            console.error(err);
        } finally {
            loading = false;
        }
    };

    const handleClick = (link: string) => {
        window.location.hash = link;
    };

    const handleStatus = (status: string) => {
        let title = status;
        let color = "217, 30, 24";

        if (status.includes("Отмен")) {
            title = "Отменена";
        }
        if (status.includes("закон")) {
            title = "Завершена";
            color = "46, 204, 113";
        }
        if (status == "Не подтверждена") {
            title = "Новая";
            color = "36, 163, 255";
        }

        return `
            <span 
                class="w-fit border rounded-[16rem] px-[20rem] py-[16rem]" 
                style="
                    background-color: rgba(${color}, 0.05); 
                    border-color: rgba(${color}, 0.8); 
                    color: rgba(${color}, 1); 
                "
            >
                ${title}
            </span>
        `;
    };
</script>

<main class="flex flex-col gap-4">
    {#if loading}
        <p class="text-gray-500">Загрузка заявок...</p>
    {:else if errorMessage}
        <p class="text-red-500">{errorMessage}</p>
    {:else}
        <div>
            <div
                class="grid grid-cols-[1fr,2fr,2fr,2fr,2fr,1fr] text-gray-600 mb-[30rem]"
            >
                <div>ID</div>
                <div>ФИО пассажира</div>
                <div>Откуда</div>
                <div>Куда</div>
                <div>Дата и время</div>
                <div>Статус</div>
            </div>
            <hr />
            <ul class="list-none p-0">
                {#each applications as app}
                    <li
                        class="grid grid-cols-[1fr,2fr,2fr,2fr,2fr,1fr] gap-[4rem] py-[26rem] hover:bg-gray-100 cursor-pointer text-[26rem] items-center"
                    >
                        <div
                            on:click={() => handleClick(`/applications/${app.id}`)}
                            class="underline text-blue-600 hover:text-blue-800 visited:text-purple-600"
                        >
                            {app.id}
                        </div>
                        <div
                            class="underline text-blue-600 hover:text-blue-800 visited:text-purple-600"
                            on:click={() => handleClick(`/passengers/${app.idPas}`)}
                        >
                            {app.fullName}
                        </div>
                        <div class="flex gap-[12rem] h-full items-center">
                            {@html createMetroStation(app.stationFrom).iconHtml}
                            {app.stationFrom?.nameStation}
                        </div>
                        <div class="flex gap-[12rem] h-full items-center">
                            {@html createMetroStation(app.stationTo).iconHtml}
                            {app.stationTo?.nameStation}
                        </div>
                        <div>{app.datetime.replace(".0", "")}</div>
                        <div class="flex justify-center w-full">
                            {@html handleStatus(app.status)}
                        </div>
                    </li>
                {/each}
            </ul>
        </div>
    {/if}
</main>
