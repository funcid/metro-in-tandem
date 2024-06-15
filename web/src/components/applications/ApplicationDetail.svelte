<script lang="ts">
    import { onMount } from "svelte";
    import { JWT } from "../login/Login.svelte";
    import { PUBLIC_API_HOST } from "$env/static/public";
    import { statusOptions, metroStations, findMetroStationByName, findMetroStationById } from "../Variables.svelte";
    import Flatpickr from "svelte-flatpickr";
    import "flatpickr/dist/flatpickr.css";

    let application: ApplicationDetailResponse | null = null;
    let status: string = "";
    let loading: boolean = true;
    let errorMessage: string = "";
    let stationStart: string = "";
    let stationEnd: string = "";
    let datetime: string = "";

    const getIdFromUrl = () => {
        const url = window.location.hash;
        const parts = url.split("/");
        return parts[2]; // Третья часть URL содержит id
    };

    function handleDateChange(event: any) {
        const [selectedDates, dateStr] = event.detail;
        // Format the date as dd.MM.yyyy HH:mm:ss
        if (selectedDates.length > 0) {
            const date = selectedDates[0];
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, "0");
            const day = String(date.getDate()).padStart(2, "0");
            const hours = String(date.getHours()).padStart(2, "0");
            const minutes = String(date.getMinutes()).padStart(2, "0");
            const seconds = String(date.getSeconds()).padStart(2, "0");
            datetime = `${day}.${month}.${year} ${hours}:${minutes}:${seconds}`;
        }
    }

    const id = getIdFromUrl();

    onMount(async () => {
        await fetchApplication();
    });

    const fetchApplication = async () => {
        loading = true;
        try {
            const response = await fetch(
                PUBLIC_API_HOST + `api/v1/applications/${id}`,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${$JWT}`,
                        "Content-Type": "application/json",
                    },
                },
            );
            if (!response.ok) {
                throw new Error("Failed to fetch application");
            }
            application = await response.json();
            status = application!.status || "";
            stationStart = findMetroStationById(application!.idSt1)?.nameStation!
            stationEnd = findMetroStationById(application!.idSt2)?.nameStation!
        } catch (err) {
            errorMessage =
                "Failed to load application. Please try again later.";
            console.error(err);
        } finally {
            loading = false;
        }
    };

    const updateApplication = async () => {
        try {
            application!.idSt1 = findMetroStationByName(stationStart)!.id;
            application!.idSt2 = findMetroStationByName(stationEnd)!.id;
            application!.datetime = datetime;
            
            const response = await fetch(
                PUBLIC_API_HOST + `api/v1/applications/${id}`,
                {
                    method: "PUT",
                    headers: {
                        Authorization: `Bearer ${$JWT}`,
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(application),
                },
            );
            if (!response.ok) {
                throw new Error("Failed to update application");
            }
            window.location.hash = `/applications`;
        } catch (err) {
            errorMessage =
                "Failed to update application. Please try again later.";
            console.error(err);
        }
    };

    const deleteApplication = async () => {
        try {
            const response = await fetch(
                PUBLIC_API_HOST + `api/v1/applications/${id}`,
                {
                    method: "DELETE",
                    headers: {
                        Authorization: `Bearer ${$JWT}`,
                        "Content-Type": "application/json",
                    },
                },
            );
            if (!response.ok) {
                throw new Error("Failed to delete application");
            }
            window.location.hash = `/applications`;
        } catch (err) {
            errorMessage =
                "Failed to delete application. Please try again later.";
            console.error(err);
        }
    };
</script>

<main class="flex flex-col justify-center items-center">
    <p class="font-bold text-[40rem] mb-[20rem]">
        Просмотр/редактирование заявки
    </p>
    <div
        class="flex justify-center bg-white border border-gray-300 shadow-md w-3/4 rounded-[30rem]"
    >
        {#if loading}
            <p class="text-gray-500">Loading application...</p>
        {:else if errorMessage}
            <p class="text-red-500">{errorMessage}</p>
        {:else if application}
            <div class="flex justify-between p-[40rem] w-full gap-[40rem]">
                <div class="w-1/2 flex flex-col justify-between">
                    <p class="font-bold">Информация:</p>
                    <div
                        class="flex flex-col justify-between h-full py-[40rem]"
                    >
                        <div>
                            <p>ID: {id}<br /></p>
                            <p>ФИО: {application.fio}</p>
                            <p>Категория: {application.catPas}</p>
                            <p>Дата создания: {application.tpz}</p>
                        </div>
                        <div>
                            <p>
                                {application.station1Name} ➜ {application.station2Name}
                            </p>
                            <p>Время прибытия: {application.timeOver}</p>
                            <p>Оценка: {application.duration}</p>
                        </div>
                        <div>
                            <p>Статус: {status}</p>
                        </div>
                    </div>
                    <button
                        type="button"
                        on:click={deleteApplication}
                        class="bg-[#D4212D] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full"
                    >
                        Удалить заявку
                    </button>
                </div>
                <form
                    class="flex flex-col gap-[12rem] w-1/2"
                    on:submit|preventDefault={updateApplication}
                >
                    <div class="mb-4">
                        <label class="block text-gray-700">Новый статус</label>
                        <select
                            bind:value={application.status}
                            class="shadow appearance-none border rounded-[12rem] p-[12rem] w-full text-gray-700"
                        >
                            {#each statusOptions as { value, label }}
                                <option value={label}>{label}</option>
                            {/each}
                        </select>
                    </div>
                    <div class="mb-4">
                        <label class="block text-gray-700">Отправление в</label>
                        <Flatpickr
                            on:change={handleDateChange}
                            options={{
                                enableTime: true,
                                dateFormat: "d.m.Y H:i:S",
                                noCalendar: false,
                                time_24hr: true,
                            }}
                            bind:value={application.datetime}
                            class="shadow appearance-none border rounded-[12rem] p-[12rem] w-full text-gray-700"
                        />
                    </div>
                    <div class="flex flex-col mb-4">
                        <label class="block text-gray-700"
                            >Фактическое отправление/прибытие</label
                        >
                        <div class="flex justify-between">
                            <input
                                type="text"
                                bind:value={application.time3}
                                class="shadow appearance-none border rounded-[12rem] p-[12rem] w-2/5 text-gray-700"
                            />
                            <input
                                type="text"
                                bind:value={application.time4}
                                class="shadow appearance-none border rounded-[12rem] p-[12rem] w-2/5 text-gray-700"
                            />
                        </div>
                    </div>
                    <div class="flex flex-col mb-4">
                        <label class="block text-gray-700"
                            >Сопровождающих мужчин/женжин</label
                        >
                        <div class="flex justify-between">
                            <input
                                type="number"
                                min="0"
                                bind:value={application.inspSexM}
                                class="shadow appearance-none border rounded-[12rem] p-[12rem] w-2/5 text-gray-700"
                                required
                            />
                            <input
                                type="number"
                                min="0"
                                bind:value={application.inspSexF}
                                class="shadow appearance-none border rounded-[12rem] p-[12rem] w-2/5 text-gray-700"
                                required
                            />
                        </div>
                    </div>
                    <div class="flex flex-col gap-[16rem] mb-4">
                        <label class="block text-gray-700">
                            Станция отправления
                        </label>
                        <select
                            id="category"
                            bind:value={stationStart}
                            class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            required
                        >
                            <option value="" disabled selected>⏵ Выберите</option>
                            {#each metroStations as { nameStation }}
                                <option>{nameStation}</option>
                            {/each}
                        </select>
                        <label class="block text-gray-700">
                            Станция прибытия
                        </label>
                        <select
                            id="category"
                            bind:value={stationEnd}
                            class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            required
                        >
                            <option value="" disabled selected>⏵ Выберите</option>
                            {#each metroStations as { nameStation }}
                                <option>{nameStation}</option>
                            {/each}
                        </select>
                    </div>
                    <div class="flex space-x-4 mt-[30rem]">
                        <button
                            type="submit"
                            class="bg-blue-500 hover:bg-blue-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full"
                        >
                            Сохранить изменения
                        </button>
                    </div>
                </form>
            </div>
        {/if}
    </div>
</main>
