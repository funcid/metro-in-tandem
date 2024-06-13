<script lang="ts">
    import { onMount } from 'svelte';
    import { JWT } from '../login/Login.svelte';

    let passenger: PassengerDetailResponse | null = null;
    let errorMessage: string = '';
    let datetime = '';
    let inspSexM = '';
    let loading: boolean = true;
    let inspSexF = '';
    let idSt1 = '';
    let idSt2 = '';

    const getIdFromUrl = () => {
        const url = window.location.hash;
        const parts = url.split('/');
        return parts[2]; // Третья часть URL содержит id
    }
    
    const id = getIdFromUrl();

    onMount(async () => {
        await fetchPassenger();
    });

    const navigateToPassengerDetail = () => {
        window.location.hash = `/passengers/${id}`;
    };

    const createEscortRequest = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/applications`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${$JWT}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ 
                    idPas: id,
                    dateTime: datetime,
                    catPas: passenger?.category,
                    status: 'Не подтверждена',
                    // tpz: , и чё тут передавать
                    inspSexM: inspSexM,
                    inspSexF: inspSexF,
                    // timeOver: , а тут
                    idSt1: idSt1,
                    idSt2: idSt2
                })
            });
            if (!response.ok) {
                throw new Error('Failed to create escort request');
            }
            alert('Escort request created successfully.');
        } catch (err) {
            errorMessage = 'Failed to create escort request. Please try again later.';
            console.error(err);
        }
    };

    const fetchPassenger = async () => {
        loading = true;
        try {
            const response = await fetch(`http://localhost:8080/api/v1/passengers/${id}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${$JWT}`,
                    'Content-Type': 'application/json'
                }
            });
            if (!response.ok) {
                throw new Error('Failed to fetch passenger');
            }
            passenger = await response.json();
        } catch (err) {
            errorMessage = 'Failed to load passenger. Please try again later.';
            console.error(err);
        } finally {
            loading = false;
        }
    };
</script>

<main class="flex flex-col justify-center items-center">
    <p class="font-bold text-[40rem] mb-[20rem]">Создать новую заявку на сопровождение</p>
    <div class="flex justify-center bg-white border border-gray-300 shadow-md w-3/4 rounded-[30rem]">
        {#if loading}
            <p class="text-gray-500">Loading passenger...</p>
        {:else if errorMessage}
            <p class="text-red-500">{errorMessage}</p>
        {:else if passenger}
            <div class="flex justify-between p-[40rem] w-full gap-[40rem]">
                <div class="w-1/2 flex flex-col justify-between">
                    <p class="font-bold">Информация:</p>
                    <div class="flex flex-col justify-between h-full py-[40rem]">
                        <div>
                            <p>ID: {id}<br/></p>
                            <p>ФИО: {passenger.fullName}</p>
                            <p>Категория: {passenger.category}</p>
                            <p>Пол: {passenger.gender}</p>
                            <p>Мобильный номер: {passenger.contactNumbers}</p>
                        </div>
                        <div>
                            <p>Статус: {status}</p> 
                        </div>
                    </div>
                    <button type="button" on:click={navigateToPassengerDetail} class="bg-[#D4212D] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full">
                        Отменить
                    </button>
                </div>
                <form class="flex flex-col gap-[12rem] w-1/2" on:submit|preventDefault={createEscortRequest}>
                    <div class="mb-4">
                        <label class="block text-gray-700">Отправление</label>
                        <input 
                            type="text" 
                            bind:value={datetime} 
                            class="shadow appearance-none border rounded-[12rem] p-[12rem] w-full text-gray-700" 
                        />
                    </div>
                    <div class="flex flex-col mb-4">
                        <label class="block text-gray-700">Сопровождающих мужчин/женжин</label>
                        <div class="flex justify-between">
                            <input 
                                type="number" 
                                bind:value={inspSexM} 
                                class="shadow appearance-none border rounded-[12rem] p-[12rem] w-2/5 text-gray-700" 
                            />
                            <input 
                                type="number" 
                                bind:value={inspSexF} 
                                class="shadow appearance-none border rounded-[12rem] p-[12rem] w-2/5 text-gray-700" 
                            />
                        </div>
                    </div>
                    <div class="flex flex-col mb-4">
                        <label class="block text-gray-700">Код станции отправления/прибытия</label>
                        <div class="flex justify-between">
                            <input 
                                type="text" 
                                bind:value={idSt1} 
                                class="shadow appearance-none border rounded-[12rem] p-[12rem] w-2/5 text-gray-700" 
                            />
                                <input 
                                type="text" 
                                bind:value={idSt2} 
                                class="shadow appearance-none border rounded-[12rem] p-[12rem] w-2/5 text-gray-700" 
                            />
                        </div>
                    </div>
                    <div class="flex space-x-4 mt-[30rem]">
                        <button type="submit" class="bg-blue-500 hover:bg-blue-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full">
                            Создать заявку на сопровождение
                        </button>
                    </div>
                </form>
            </div>
            {/if}
        </div>
    </main>