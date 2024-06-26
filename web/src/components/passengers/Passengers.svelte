<script lang="ts">
    import { onMount } from "svelte";
    import * as backend from "../../utils/backend";

    let passengers: PassengerResponse[] = [];
    let filteredPassengers: PassengerResponse[] = [];
    let loading: boolean = true;
    let errorMessage: string = "";
    let searchQuery: string = "";

    onMount(async () => {
        await fetchPassengers();
    });

    const fetchPassengers = async () => {
        loading = true;
        try {
            passengers = await backend.get<PassengerResponse[]>(
                `/api/v1/passengers/all`,
            );
            handleSearch();
        } catch (err) {
            errorMessage =
                "Не удалось загрузить пассажиров. Пожалуйста, попробуйте позже.";
        }
        loading = false;
    };

    const handleSearch = () => {
        const query = searchQuery.toLowerCase();
        filteredPassengers = passengers.filter(
            (pas) =>
                pas.fullName.toLowerCase().includes(query) ||
                pas.contactNumbers.some((contact) =>
                    contact.number
                        .replace(" ", "")
                        .replace("+", "")
                        .replace("(", "")
                        .replace(")", "")
                        .includes(query),
                ),
        );
    };

    const handleClick = (id: string) => {
        window.location.hash = `/passengers/${id}`;
    };

    const handleCreatePassenger = () => {
        window.location.hash = "/create-passenger";
    };

    const handleCreateApplication = (id: string) => {
        window.location.hash = `/create-application/${id}`;
    };
</script>

<main class="flex flex-col gap-[40rem]">
    <p class="font-bold text-[40rem]">Пассажиры</p>
    {#if loading}
        <p class="text-gray-500">Загрузка пассажиров...</p>
    {:else if errorMessage}
        <p class="text-red-500">{errorMessage}</p>
    {:else}
        <div class="flex flex-col gap-[40rem]">
            <div class="flex justify-between">
                <input
                    type="text"
                    placeholder="Поиск по ФИО/номеру телефона"
                    bind:value={searchQuery}
                    class="border border-gray-400 p-[20rem] rounded-[20rem] w-1/2"
                    on:input={handleSearch}
                />
                <button
                    class="bg-[#D4212D] w-[310rem] h-[82rem] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[20rem] items-center text-white"
                    on:click={handleCreatePassenger}
                >
                    Новый пассажир
                </button>
            </div>
            <hr />
            <div>
                <div
                    class="grid grid-cols-[1fr,2fr,2fr,1fr,1fr,1fr] text-gray-600 mb-[30rem]"
                >
                    <div>ID</div>
                    <div>ФИО</div>
                    <div>Телефон</div>
                    <div>Пол</div>
                    <div>Категория</div>
                    <div></div>
                </div>
                <hr />
                <ul class="list-none p-0">
                    {#each filteredPassengers as pas}
                        <li
                            class="grid grid-cols-[1fr,2fr,2fr,1fr,1fr,1fr] gap-[4rem] py-[36rem] hover:bg-gray-100 cursor-pointer text-[26rem] items-center"
                        >
                            <div
                                on:click={() => handleClick(pas.id)}
                                class="underline text-blue-600 hover:text-blue-800 visited:text-purple-600"
                            >
                                {pas.id}
                            </div>
                            <div
                                on:click={() => handleClick(pas.id)}
                                class="underline text-blue-600 hover:text-blue-800 visited:text-purple-600"
                            >
                                {pas.fullName}
                            </div>
                            <div class="flex gap-[12rem] h-full items-center">
                                {pas.contactNumbers[0]
                                    ? pas.contactNumbers[0].number
                                    : "—"}
                            </div>
                            <div class="flex gap-[12rem] h-full items-center">
                                {pas.gender}
                            </div>
                            <div class="flex gap-[12rem] h-full items-center">
                                {pas.category}
                            </div>
                            <div
                                on:click={() => handleCreateApplication(pas.id)}
                                class="underline text-blue-600 hover:text-blue-800 visited:text-purple-600"
                            >
                                Новая заявка
                            </div>
                        </li>
                    {/each}
                </ul>
            </div>
        </div>
    {/if}
</main>
