<script lang="ts">
    import { onMount } from "svelte";
    import { JWT } from "../login/Login.svelte";
    import { PUBLIC_API_HOST } from "$env/static/public";

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
            const response = await fetch(
                PUBLIC_API_HOST + `api/v1/passengers/all`,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${$JWT}`, // fixed concatenation issue
                        "Content-Type": "application/json",
                    },
                },
            );
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            passengers = await response.json();
            filteredPassengers = passengers;
        } catch (err) {
            errorMessage = "Failed to load passengers. Please try again later.";
            console.error(err);
        } finally {
            loading = false;
        }
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
</script>

<main>
    <p class="font-bold text-[40rem] mb-[20rem]">Пассажиры</p>
    {#if loading}
        <p class="text-gray-500">Загрузка пассажиров...</p>
    {:else if errorMessage}
        <p class="text-red-500">{errorMessage}</p>
    {:else}
        <div class="flex flex-col gap-[20rem]">
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
            <div class="grid grid-cols-2">
                {#each filteredPassengers as pas}
                    <div class="flex">
                        <div 
                            class="w-full bg-white ml-[12rem] mb-[12rem] border border-gray-300 shadow-md cursor-pointer hover:bg-gray-100 rounded-[20rem] p-[20rem] rounded-r-none border-r-0"
                            on:click={() => handleClick(pas.id)}
                        >
                            <div class="flex justify-between items-center">
                                <div class="flex flex-col gap-[10rem]">
                                    <div class="flex items-center">
                                        {pas.fullName}
                                        <span class="ml-[10rem]"
                                            >{pas.category}</span
                                        >
                                        {#if pas.hasPacemaker}
                                            <span
                                                class="text-red-500 ml-[10rem]"
                                                >❤️</span
                                            >
                                        {/if}
                                    </div>
                                    <div>
                                        <ul>
                                            {#each pas.contactNumbers as contact}
                                                <li>{contact.number}</li>
                                            {/each}
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <a class="
                            flex bg-gray-500 hover:bg-gray-400 mb-[12rem] border border-gray-300 shadow-md cursor-pointer mb-[12rem] 
                            min-w-[120rem] min-h-[120rem] rounded-[20rem] rounded-l-none border-l-0
                            text-center items-center text-white text-[20rem] uppercase"
                            href={`/#/create-application/${pas.id}`}
                        >
                                Новая
                                Заявка
                        </a>
                    </div>
                {/each}
            </div>
        </div>
    {/if}
</main>
