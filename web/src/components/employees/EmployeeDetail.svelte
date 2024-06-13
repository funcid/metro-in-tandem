<script lang="ts">
    import { onMount } from 'svelte';
    import { JWT } from '../login/Login.svelte';

    let employee: EmployeeDetailResponse | null = null;
    let status: string = '';
    let loading: boolean = true;
    let errorMessage: string = '';

    const getIdFromUrl = () => {
        const url = window.location.hash;
        const parts = url.split('/');
        return parts[2]; // Третья часть URL содержит id
    }
    
    const id = getIdFromUrl();

    onMount(async () => {
        await fetchEmployee();
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

    let sections = ["ЦУ-1", "ЦУ-2", "ЦУ-3", "ЦУ-3 (Н)", "ЦУ-4", "ЦУ-4 (Н)", "ЦУ-5", "ЦУ-8"];
    
    let selectedSection = sections[0];

    const fetchEmployee = async () => {
        loading = true;
        try {
            const response = await fetch(`http://localhost:8080/api/v1/employees/${id}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${$JWT}`,
                    'Content-Type': 'application/json'
                }
            });
            if (!response.ok) {
                throw new Error('Failed to fetch application');
            }
            employee = await response.json();
            status = employee?.status || '';
        } catch (err) {
            errorMessage = 'Failed to load application. Please try again later.';
            console.error(err);
        } finally {
            loading = false;
        }
    };

    const updateEmployee = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/employees/${id}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${$JWT}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(employee)
            });
            if (!response.ok) {
                throw new Error('Failed to update application');
            }
            window.location.hash = `/employees`
        } catch (err) {
            errorMessage = 'Failed to update application. Please try again later.';
            console.error(err);
        }
    };

    const deleteEmployee = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/employees/${id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${$JWT}`,
                    'Content-Type': 'application/json'
                }
            });
            if (!response.ok) {
                throw new Error('Failed to delete application');
            }
            window.location.hash = `/employees`
        } catch (err) {
            errorMessage = 'Failed to delete application. Please try again later.';
            console.error(err);
        }
    };
</script>

<main class="flex flex-col justify-center items-center">
    <p class="font-bold text-[40rem] mb-[20rem]">Просмотр/редактирование сотрудника</p>
    <div class="flex justify-center bg-white border border-gray-300 shadow-md w-3/4 rounded-[30rem]">
        {#if loading}
            <p class="text-gray-500">Loading application...</p>
        {:else if errorMessage}
            <p class="text-red-500">{errorMessage}</p>
        {:else if employee}
            <div class="flex justify-between p-[40rem] w-full gap-[40rem]">
                <div class="w-1/2 flex flex-col justify-between">
                    <p class="font-bold">Информация:</p>
                    <div class="flex flex-col justify-between h-full py-[40rem]">
                        <div>
                            <p>ID: {id}<br/></p>
                            <p>ФИО: {employee.fio}</p>
                            <p>Категория: {employee.catPas}</p>
                            <p>Дата создания: {employee.tpz}</p>
                        </div>
                        <div>
                            <p>{employee.station1Name} ➜ {employee.station2Name}</p>
                            <p>Время прибытия: {employee.timeOver}</p>
                            <p>Оценка: {employee.duration}</p>
                        </div>
                        <div>
                            <p>Статус: {status}</p> 
                        </div>
                    </div>
                    <button type="button" on:click={deleteEmployee} class="bg-[#D4212D] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full">
                        Удалить заявку
                    </button>
                </div>
                <form class="flex flex-col gap-[12rem] w-1/2" on:submit|preventDefault={updateEmployee}>
                    <div class="mb-4">
                        <label class="block text-gray-700">Участок</label>
                        <select 
                            bind:value={employee.status} 
                            class="shadow appearance-none border rounded-[12rem] p-[12rem] w-full text-gray-700"
                        >
                            {#each sections as section}
                                <option value={section}>{section}</option>
                            {/each}
                        </select>
                    </div>
                    <div class="mb-4">
                        <label class="block text-gray-700">Рабочее время</label>
                        <input 
                            type="text" 
                            bind:value={employee.timeWork} 
                            class="shadow appearance-none border rounded-[12rem] p-[12rem] w-full text-gray-700" 
                        />
                    </div>
                    <div class="flex space-x-4 mt-[30rem]">
                        <button type="submit" class="bg-blue-500 hover:bg-blue-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full">
                            Сохранить изменения
                        </button>
                    </div>
                </form>
            </div>
            {/if}
        </div>
    </main>