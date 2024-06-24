<script lang="ts">
    import { createMetroStation } from "../../utils/metro";
    import { onMount } from "svelte";
    import { JWT } from "../../utils/auth";
    import { PUBLIC_API_HOST } from "$env/static/public";
    import Flatpickr from "svelte-flatpickr";

    let applications: ApplicationResponse[] = [];
    let loading: boolean = true;
    let errorMessage: string = "";
    let time: string = "2024-04-24";
    let searchQuery: string = "";

    onMount(async () => {
        await searchApplications();
    });

    const searchApplications = async () => {
        try {
            const formattedTime = formatDate(time);
            const response = await fetch(
                `${PUBLIC_API_HOST}api/v1/applications/search?date=${formattedTime}&namePrefix=${searchQuery}`,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${$JWT}`,
                        "Content-Type": "application/json",
                    },
                },
            );
            if (!response.ok) {
                throw new Error("Failed to search applications");
            }
            applications = await response.json();
        } catch (err) {
            errorMessage = "Failed to search applications. Please try again later.";
            console.error(err);
        } finally {
            loading = false;
        }
    };

    const handleClick = (link: string) => {
        window.location.hash = link;
    };

    const formatDate = (dateString: string) => {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, "0");
        const day = date.getDate().toString().padStart(2, "0");
        return `${year}-${month}-${day}`;
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
        if (status === "Не подтверждена") {
            title = "Новая";
            color = "36, 163, 255";
        }

        return `
            <span 
                class="w-fit border rounded-[16rem] px-[14rem] py-[10rem]" 
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

    function handleDatetime(datetime: string) {
        return datetime.replace(".0", "").split(" ")[1].slice(0, 5);
    }

    const handleSearchInputChange = async () => {
        await searchApplications();
    };

    async function handleDateChange(event: any) {
        const [selectedDates, dateStr] = event.detail;
        if (selectedDates.length > 0) {
            time = dateStr;
            await searchApplications();
        }
    }
</script>

<main class="flex flex-col gap-[40rem]">
    <p class="font-bold text-[40rem]">Заявки</p>
    {#if loading}
        <p class="text-gray-500">Загрузка заявок...</p>
    {:else if errorMessage}
        <p class="text-red-500">{errorMessage}</p>
    {:else}
        <div class="flex justify-between">
            <input
                type="text"
                bind:value={searchQuery}
                on:input={handleSearchInputChange}
                placeholder="Поиск по ФИО"
                class="border border-gray-400 p-[20rem] rounded-[20rem] w-1/2"
            />
            <Flatpickr
                options={{
                    dateFormat: "Y.m.d",
                    noCalendar: false,
                    time_24hr: true,
                }}
                bind:value={time}
                on:change={handleDateChange}
                class="flex w-fit border text-gray-700 border-gray-400 p-[20rem] rounded-[20rem]"
            />
        </div>
        {#if applications.length == 0}
            <p>Заявки не найдены</p>
        {:else}
            <hr />
            <div>
                <div class="grid grid-cols-[1fr,2fr,2fr,2fr,1fr,1fr,1fr] text-gray-600 mb-[30rem] text-[32rem]">
                    <div class="hidden md:block">ID</div>
                    <div>ФИО</div>
                    <div>Откуда</div>
                    <div>Куда</div>
                    <div>Время</div>
                    <div>Работник</div>
                    <div>Статус</div>
                </div>
                <hr />
                <ul class="list-none p-0">
                    {#each applications as app}
                        <li class="grid grid-cols-[1fr,2fr,2fr,2fr,1fr,1fr,1fr] gap-[2rem] py-[26rem] hover:bg-gray-100 cursor-pointer text-[26rem] items-center">
                            <div on:click={() => handleClick(`/applications/${app.id}`)} class="hidden md:block underline text-blue-600 hover:text-blue-800 visited:text-purple-600">
                                {app.id}
                            </div>
                            <div class="underline text-blue-600 hover:text-blue-800 visited:text-purple-600" on:click={() => handleClick(`/passengers/${app.idPas}`)}>
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
                            <div>{handleDatetime(app.datetime)}</div>
                            <div class="underline text-blue-600 hover:text-blue-800 visited:text-purple-600" on:click={() => handleClick(`/employees/${app.employeeId}`)}>
                                {app.employeeFio?.split(' ')[0]}
                            </div>
                            <div class="flex justify-center w-full">
                                {@html handleStatus(app.status)}
                            </div>
                        </li>
                    {/each}
                </ul>
            </div>
        {/if}
    {/if}
</main>
