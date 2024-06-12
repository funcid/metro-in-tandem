<script lang="ts">
    import { onMount } from 'svelte';
    import { JWT } from '../login/Login.svelte';

    let application: ApplicationDetailResponse | null = null;
    let status: string = '';
    let loading: boolean = true;
    let errorMessage: string = '';
    let successMessage: string = '';

    const getIdFromUrl = () => {
        const url = window.location.hash;
        const parts = url.split('/');
        return parts[2]; // Третья часть URL содержит id
    }
    
    const id = getIdFromUrl();

    onMount(async () => {
        await fetchApplication();
    });

    const statusOptions = [
        { value: "REQUEST_COMPLETED", label: "Заявка закончена" },
        { value: "NOT_APPROVED", label: "Не подтверждена" },
        { value: "CANCELLED_BY_PASSENGER", label: "Отмена заявки по просьбе пассажира" },
        { value: "NOT_VISITED_BY_PASSENGER", label: "Отмена заявки по неявке пассажира" },
        { value: "ACCEPTED", label: "Принята" },
        { value: "INSPECTOR_ON_THE_WAY", label: "Инспектор выехал" },
        { value: "INSPECTOR_ARRIVED", label: "Инспектор на месте" },
        { value: "TRIP", label: "Поездка" },
        { value: "PASSENGER_DELAYED", label: "Пассажир опаздывает" },
        { value: "INSPECTOR_DELAYED", label: "Инспектор опаздывает" }
    ];

    const fetchApplication = async () => {
        loading = true;
        try {
            const response = await fetch(`http://localhost:8080/api/v1/applications/${id}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${$JWT}`,
                    'Content-Type': 'application/json'
                }
            });
            if (!response.ok) {
                throw new Error('Failed to fetch application');
            }
            application = await response.json();
            status = application?.status || '';
        } catch (err) {
            errorMessage = 'Failed to load application. Please try again later.';
            console.error(err);
        } finally {
            loading = false;
        }
    };

    const updateApplication = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/applications/${id}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${$JWT}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(application)
            });
            if (!response.ok) {
                throw new Error('Failed to update application');
            }
            window.location.hash = `/applications`
        } catch (err) {
            errorMessage = 'Failed to update application. Please try again later.';
            console.error(err);
        }
    };

    const deleteApplication = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/applications/${id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${$JWT}`,
                    'Content-Type': 'application/json'
                }
            });
            if (!response.ok) {
                throw new Error('Failed to delete application');
            }
            successMessage = 'Application deleted successfully!';
            setTimeout(() => window.location.hash = `/applications`, 2000); // Redirect to applications page after 2 seconds
        } catch (err) {
            errorMessage = 'Failed to delete application. Please try again later.';
            console.error(err);
        }
    };
</script>

<main class="flex flex-col justify-center items-center">
    <p class="font-bold text-[40rem] mb-[20rem]">Просмотр/редактирование заявки</p>
    <div class="flex justify-center bg-white border border-gray-300 shadow-md w-3/4 rounded-[30rem]">
        {#if loading}
            <p class="text-gray-500">Loading application...</p>
        {:else if errorMessage}
            <p class="text-red-500">{errorMessage}</p>
        {:else if application}
            <div class="flex justify-between p-[40rem] w-full gap-[40rem]">
                <div class="w-1/2 flex flex-col justify-between">
                    <p class="font-bold">Информация:</p>
                    <div class="flex flex-col justify-between h-full py-[40rem]">
                        <div>
                            <p>ID: {id}<br/></p>
                            <p>ФИО: {application.fio}</p>
                            <p>Категория: {application.catPas}</p>
                            <p>Дата создания: {application.tpz}</p>
                        </div>
                        <div>
                            <p>{application.station1Name} ➜ {application.station2Name}</p>
                            <p>Время прибытия: {application.timeOver}</p>
                            <p>Оценка: {application.duration}</p>
                        </div>
                        <div>
                            <p>Статус: {status}</p> 
                        </div>
                    </div>
                    <button type="button" on:click={deleteApplication} class="bg-[#D4212D] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full">
                        Удалить заявку
                    </button>
                </div>
                <form class="flex flex-col gap-[12rem] w-1/2" on:submit|preventDefault={updateApplication}>
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
                        <label class="block text-gray-700">Отправление</label>
                        <input 
                            type="text" 
                            bind:value={application.datetime} 
                            class="shadow appearance-none border rounded-[12rem] p-[12rem] w-full text-gray-700" 
                        />
                    </div>
                    <div class="flex flex-col mb-4">
                        <label class="block text-gray-700">Фактическое отправление/прибытие</label>
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
                        <label class="block text-gray-700">Сопровождающих мужчин/женжин</label>
                        <div class="flex justify-between">
                            <input 
                                type="number" 
                                bind:value={application.inspSexM} 
                                class="shadow appearance-none border rounded-[12rem] p-[12rem] w-2/5 text-gray-700" 
                            />
                            <input 
                                type="number" 
                                bind:value={application.inspSexF} 
                                class="shadow appearance-none border rounded-[12rem] p-[12rem] w-2/5 text-gray-700" 
                            />
                        </div>
                    </div>
                    <div class="flex flex-col mb-4">
                        <label class="block text-gray-700">Код станции отправления/прибытия</label>
                        <div class="flex justify-between">
                            <input 
                                type="text" 
                                bind:value={application.idSt1} 
                                class="shadow appearance-none border rounded-[12rem] p-[12rem] w-2/5 text-gray-700" 
                            />
                                <input 
                                type="text" 
                                bind:value={application.idSt2} 
                                class="shadow appearance-none border rounded-[12rem] p-[12rem] w-2/5 text-gray-700" 
                            />
                        </div>
                    </div>
                    <div class="flex space-x-4 mt-[30rem]">
                        <button type="submit" class="bg-[#D4212D] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full">
                            Сохранить изменения
                        </button>
                    </div>
                </form>
                {#if successMessage}
                    <p class="text-green-500 mt-2">{successMessage}</p>
                {/if}
            </div>
            {/if}
        </div>
    </main>