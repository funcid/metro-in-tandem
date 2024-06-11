<script lang="ts">
    import { onMount } from 'svelte';
    import { JWT } from '../login/Login.svelte';

    let applications: ApplicationResponse[] = [];
    let loading: boolean = true;
    let errorMessage: string = '';

    onMount(async () => {
        await fetchApplications();
    });

    const fetchApplications = async () => {
        loading = true;
        try {
            const response = await fetch(`http://localhost:8080/api/v1/applications`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${$JWT}`,
                    'Content-Type': 'application/json'
                }
            });
            if (!response.ok) {
                return;
            }
            applications = await response.json();
        } catch (err) {
            errorMessage = 'Failed to load applications. Please try again later.';
            console.error(err);
        } finally {
            loading = false;
        }
    };

    // Функция для обработки клика по заявке
    const handleClick = (id: string) => {
        window.location.hash = `/applications/${id}`;
    };

    const calculateMinutesSince = (datetime: string): number => {
        const now = new Date();
        const date = new Date(datetime);
        const diffMs = now.getTime() - date.getTime();
        return Math.floor(diffMs / 60000); // Переводим из миллисекунд в минуты
    };
</script>

<main>
    {#if loading}
        <p class="text-gray-500">Loading applications...</p>
    {:else if errorMessage}
        <p class="text-red-500">{errorMessage}</p>
    {:else}
    <div>
        <ul class="list-none p-0">
            {#each applications as app}
                <li class="p-4 mb-[20rem] bg-white border border-gray-400 shadow-md cursor-pointer hover:bg-gray-100 rounded-[20rem] p-[20rem]" on:click={() => handleClick(app.id)}>
                    <div class="flex justify-between items-center">
                        <div class="flex flex-col gap-[12rem]">
                            <div><span class="font-bold">ФИО:</span> {app.fullName}</div>
                            <div><span class="font-bold">Контакт:</span> {app.mobileNumber}</div>
                            <div><span class="font-bold">Категория:</span> {app.catPas}</div>
                            <div><span class="font-bold">Статус:</span> {app.status}</div>
                        </div>
                            <div class="text-right">
                                {#if app.status.includes('Отмена')}
                                    <span class="text-red-500">Отменена</span>
                                {:else if app.status.includes('Не подтверждена')}
                                    <span class="text-[#00bfff]">Ожидает {calculateMinutesSince(app.datetime)} мин.</span>
                                {:else}
                                    {app.time3} ➜ {app.time4}
                                {/if}
                        </div>
                    </div>
                </li>
            {/each}
        </ul>
    </div>
{/if}
</main>