@ -0,0 +1,191 @@
<script lang="ts">
    import { onMount } from 'svelte';
    import { JWT } from '../login/Login.svelte';
    import Flatpickr from 'svelte-flatpickr';
    import 'flatpickr/dist/flatpickr.css';

    let passenger: PassengerDetailResponse | null = null;

    let datetime = '';
    let inspSexM = '';
    let inspSexF = '';
    let idSt1 = '';
    let idSt2 = '';

    let loading: boolean = true;
    let errorMessage: string = '';

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
                    datetime: datetime,
                    inspSexM: inspSexM,
                    inspSexF: inspSexF,
                    idSt1: idSt1,
                    idSt2: idSt2
                })
            });
            if (!response.ok) {
                throw new Error('Failed to create escort request');
            }
            let created = await response.json();
            window.location.hash = `/applications/${created.id}`
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

    function handleDateChange(event: any) {
		const [selectedDates, dateStr] = event.detail;
		// Format the date as dd.MM.yyyy HH:mm:ss
        if (selectedDates.length > 0) {
            const date = selectedDates[0];
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            const seconds = String(date.getSeconds()).padStart(2, '0');
            datetime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
        }
	}
</script>

<main class="flex flex-col justify-center items-center">
    <p class="font-bold text-[40rem] mb-[20rem]">Создать новую заявку на сопровождение</p>
    <div class="flex justify-center bg-white border border-gray-300 shadow-md w-2/3 rounded-[30rem]">
        {#if loading}
            <p class="text-gray-500">Loading passenger...</p>
        {:else if errorMessage}
            <p class="text-red-500">{errorMessage}</p>
        {:else if passenger}
            <div class="flex justify-between p-[40rem] w-full gap-[40rem]">
                <div class="w-1/2 flex flex-col justify-between">
                    <p class="font-bold">Информация:</p>
                    <div class="flex flex-col h-full py-[40rem]">
                        <p>ID: {id}<br/></p>
                        <p>ФИО: {passenger.fullName}</p>
                        <p>Категория: {passenger.category}</p>
                        <p>Пол: {passenger.gender}</p>


                        <br/>
                        <p>Мобильный номера:</p>
                        {#each passenger.contactNumbers as contact, index}
                            <div class="flex flex-col">
                                {contact.number} 
                            </div>
                        {/each}
                    </div>
                    <button type="button" on:click={navigateToPassengerDetail} class="bg-[#D4212D] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full">
                        Отменить
                    </button>
                </div>
                <form class="flex flex-col gap-[12rem] w-1/2" on:submit|preventDefault={createEscortRequest}>
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
                        <label class="block text-gray-700">Сопровождающих мужчин/женжин</label>
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
                    <div class="flex flex-col mb-4">
                        <label class="block text-gray-700">Код станции отправления/прибытия</label>
                        <div class="flex justify-between">
                            <input 
                                type="number" 
                                min="0"
                                bind:value={idSt1} 
                                class="shadow appearance-none border rounded-[12rem] p-[12rem] w-2/5 text-gray-700"
                                required 
                            />
                            <input 
                                type="number" 
                                min="0"
                                bind:value={idSt2} 
                                class="shadow appearance-none border rounded-[12rem] p-[12rem] w-2/5 text-gray-700"
                                required 
                            />
                        </div>
                    </div>
                    <div class="flex space-x-4 mt-[30rem]">
                        <button type="submit" class="bg-blue-500 hover:bg-blue-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full">
                            Создать
                        </button>
                    </div>
                </form>
            </div>
        {/if}
    </div>
</main>