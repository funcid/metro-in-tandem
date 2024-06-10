<script lang="ts">
    import { onMount } from 'svelte';
    import { getRandomIcon } from '../constants/icons';

    export let background: string;
    export let title: string;
    export let description: string;
    export let color: string;
    export let blockSize: number;
    export let height: number = 519;
    export let additionalStyle: string = '';
    export let url: string;

    // Определяем переменную с типом Application[]
    let applications: Application[] = [];
    let loading: boolean = true;
    let errorMessage: string = '';

    // Функция для получения данных
    async function fetchApplications(): Promise<void> {
        let tokenValue: string | null;

        JWT.subscribe(value => {
            tokenValue = value;
        });

        if (!tokenValue) {
            errorMessage = 'User is not authenticated';
            loading = false;
            return;
        }

        try {
            const response = await fetch('https://localhost:8080/api/v1/applications', {
                headers: {
                    'Authorization': `Bearer ${tokenValue}`
                }
            });

            if (response.ok) {
                const data = await response.json() as Application[];
                applications = data;
            } else {
                errorMessage = `Failed to fetch applications: ${response.status}`;
            }
        } catch (error) {
            errorMessage = `Error fetching applications: ${error.message}`;
        } finally {
            loading = false;
        }
    }

    // Выполняем запрос при монтировании компонента
    onMount(() => {
        fetchApplications();
    });
</script>

<!-- Button элемент -->
<button on:click={() => window.open(url)} class="project-item text-left w-full rounded-[7rem] overflow-hidden" style="height: {height}rem; {additionalStyle}; color: var({color})">
    <img
        src={background}
        class="w-full object-cover object-center"
        alt="{background} logo"
        style="height: calc(100% - {blockSize}rem)"
    />
    <div class="w-full" style="color: black; background: var({color}); height: {blockSize}rem">
        <div class="p-[30rem] h-full flex justify-between gap-[20rem]">
            <div class="w-full flex flex-col justify-between">
                <p class="text-[72rem] font-bold font-oddval">
                    {@html title}
                </p>
                <p class="w-full text-[32rem] font-onest">
                    {description}
                </p>
            </div>
            <div class="w-[22rem] h-full flex flex-col justify-between">
                {@html getRandomIcon()}
                {@html getRandomIcon()}
            </div>
        </div>
    </div>
</button>

<!-- Логика отображения в зависимости от состояния загрузки и данных -->
{#if loading}
    <p>Loading applications...</p>
{:else if errorMessage}
    <p>{errorMessage}</p>
{:else}
    <ul>
        {#each applications as app}
            <li>
                <span>ID: {app.id}</span>
                <span>ID PAS: {app.id_pas}</span>
                <!-- Добавь тут другие поля, если нужно -->
            </li>
        {/each}
    </ul>
{/if}