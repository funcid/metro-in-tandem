<script lang="ts">
    import { onMount } from "svelte";
    import { JWT } from "../login/Login.svelte";
    import { PUBLIC_API_HOST } from "$env/static/public";
    import { metroStations, findMetroStationByName, dateToTimestamp } from "../Variables.svelte";
    import Flatpickr from "svelte-flatpickr";
    import "flatpickr/dist/flatpickr.css";

    let passenger: PassengerDetailResponse | null = null;

    let datetime = "";
    let inspSexM = 0;
    let inspSexF = 0;
    let stationStart = "";
    let stationEnd = "";

    let loading: boolean = true;
    let errorMessage: string = "";

    const getIdFromUrl = () => {
        const url = window.location.hash;
        const parts = url.split("/");
        return parts[2]; // Третья часть URL содержит id
    };

    const id = getIdFromUrl();

    onMount(async () => {
        await fetchPassenger();
    });

    const navigateToPassengerDetail = () => {
        window.location.hash = `/passengers/${id}`;
    };

    const createEscortRequest = async () => {
        try {
            const response = await fetch(
                PUBLIC_API_HOST + `api/v1/applications`,
                {
                    method: "POST",
                    headers: {
                        Authorization: `Bearer ${$JWT}`,
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        idPas: id,
                        datetime: datetime,
                        inspSexM: inspSexM,
                        inspSexF: inspSexF,
                        idSt1: findMetroStationByName(stationStart)?.id,
                        idSt2: findMetroStationByName(stationEnd)?.id,
                    }),
                },
            );
            if (!response.ok) {
                throw new Error("Failed to create escort request");
            }
            let created = await response.json();
            window.location.hash = `/applications/${created.id}`
        } catch (err) {
            errorMessage =
                "Failed to create escort request. Please try again later.";
            console.error(err);
        }
    };

    const fetchPassenger = async () => {
        loading = true;
        try {
            const response = await fetch(
                PUBLIC_API_HOST + `api/v1/passengers/${id}`,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${$JWT}`,
                        "Content-Type": "application/json",
                    },
                },
            );
            if (!response.ok) {
                throw new Error("Failed to fetch passenger");
            }
            passenger = await response.json();
        } catch (err) {
            errorMessage = "Failed to load passenger. Please try again later.";
            console.error(err);
        } finally {
            loading = false;
        }
    };

    function handleDateChange(event: any) {
        const [selectedDates, dateStr] = event.detail;
        if (selectedDates.length > 0) {
            datetime = dateToTimestamp(selectedDates[0])
        }
    }
</script>

<main class="flex flex-col justify-center items-center">
    <p class="font-bold text-[40rem] mb-[20rem]">
        Создать новую заявку на сопровождение
    </p>
    <div
        class="flex w-full lg:w-[auto] justify-center bg-white border border-gray-300 shadow-md w-2/3 rounded-[30rem]"
    >
        {#if loading}
            <p class="text-gray-500">Loading passenger...</p>
        {:else if errorMessage}
            <p class="text-red-500">{errorMessage}</p>
        {:else if passenger}
            <div class="flex justify-between p-[40rem] w-full gap-[40rem]">
                <div class="w-1/2 flex flex-col justify-between">
                    <p class="font-bold">Информация:</p>
                    <div class="flex flex-col h-full py-[40rem]">
                        <p>ID: {id}<br /></p>
                        <p>ФИО: {passenger.fullName}</p>
                        <p>Категория: {passenger.category}</p>
                        <p>Пол: {passenger.gender}</p>

                        <br />
                        <p>Мобильный номера:</p>
                        {#each passenger.contactNumbers as contact, index}
                            <div class="flex flex-col">
                                {contact.number}
                            </div>
                        {/each}
                    </div>
                    <button
                        type="button"
                        on:click={navigateToPassengerDetail}
                        class="bg-[#D4212D] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full"
                    >
                        Отменить
                    </button>
                </div>
                <form
                    class="flex flex-col gap-[12rem] w-1/2"
                    on:submit|preventDefault={createEscortRequest}
                >
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
                            class="shadow appearance-none border rounded-[12rem] p-[12rem] w-full text-gray-700"
                        />
                    </div>
                    <div class="flex flex-col mb-4">
                        <label class="block text-gray-700"
                            >Сопровождающих мужчин/женжин</label
                        >
                        <div class="flex justify-between">
                            <input
                                type="number"
                                min="0"
                                bind:value={inspSexM}
                                class="shadow appearance-none border rounded-[12rem] p-[12rem] w-2/5 text-gray-700"
                                required
                            />
                            <input
                                type="number"
                                min="0"
                                bind:value={inspSexF}
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
                            class="bg-blue-500 h-[88rem] hover:bg-blue-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full"
                        >
                            Создать
                        </button>
                    </div>
                </form>
            </div>
        {/if}
    </div>
</main>
