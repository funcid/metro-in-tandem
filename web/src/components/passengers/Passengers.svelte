<script lang="ts">
    import { onMount } from 'svelte';
    import { JWT } from '../login/Login.svelte';

    let passengers: PassengerResponse[] = [];
    let filteredPassengers: PassengerResponse[] = [];
    let loading: boolean = true;
    let errorMessage: string = '';
    let searchQuery: string = '';

    onMount(async () => {
        await fetchPassengers();
    });

    const fetchPassengers = async () => {
        loading = true;
        try {
            const response = await fetch('http://localhost:8080/api/v1/passengers/all', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${$JWT}`,  // fixed concatenation issue
                    'Content-Type': 'application/json'
                }
            });
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            passengers = await response.json();
            filteredPassengers = passengers;
        } catch (err) {
            errorMessage = 'Failed to load passengers. Please try again later.';
            console.error(err);
        } finally {
            loading = false;
        }
    };

    const handleSearch = () => {
        const query = searchQuery.toLowerCase();
        filteredPassengers = passengers.filter(pas =>
            pas.fullName.toLowerCase().includes(query) ||
            pas.contactNumbers.some(contact => contact.number
                .replace(' ', '')
                .replace('+', '')
                .replace('(', '')
                .replace(')', '')
                .includes(query)
            )
        );
    };

    const handleClick = (id: string) => {
        window.location.hash = `/passengers/${id}`;
    };

    const handleCreatePassenger = () => {
        window.location.hash = '/create-passenger';
    };
</script>

<main>
    {#if loading}
        <p class="text-gray-500">Загрузка пассажиров...</p>
    {:else if errorMessage}
        <p class="text-red-500">{errorMessage}</p>
    {:else}
        <div class="flex flex-col gap-[20rem]">
            <div class="flex justify-between items-center">
                <input
                    type="text"
                    placeholder="Поиск по ФИО/номеру телефона"
                    bind:value={searchQuery}
                    class="border border-gray-400 py-[12rem] px-[26rem] rounded-[12rem] w-1/2"
                    on:input={handleSearch} />
                <button
                    class="flex gap-[20rem] bg-[#D4212D] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white"
                    on:click={handleCreatePassenger}>
                    Создать пассажира
                </button>
            </div>
            <hr/>
            <div class="grid grid-cols-2">
                {#each filteredPassengers as pas}
                    <div class="bg-white ml-[12rem] mb-[12rem] border border-gray-400 shadow-md cursor-pointer hover:bg-gray-100 rounded-[20rem] p-[20rem]" on:click={() => handleClick(pas.id)}>
                        <div class="flex justify-between items-center">
                            <div class="flex flex-col gap-[10rem]">
                                <div class="flex items-center">
                                    <span class="font-bold">{pas.fullName}</span>
                                    <span class="ml-[10rem]">{pas.category}</span>
                                    {#if pas.hasPacemaker}
                                        <span class="text-red-500 ml-[10rem]">❤️</span>
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
                    {/each}
                </div>
            </div>
        {/if}
    </main>