<script lang="ts">
    import { onMount } from 'svelte';
    import { JWT } from '../login/Login.svelte';

    let application: ApplicationDetailResponse | null = null;
    let loading: boolean = true;
    let errorMessage: string = '';

    const getIdFromUrl = () => {
        const url = window.location.hash;
        const parts = url.split('/');
        return parts[2]; // Третья часть URL содержит id
    }
    
    const id = getIdFromUrl();

    onMount(async () => {
        await fetchApplication();
    });

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
        } catch (err) {
            errorMessage = 'Failed to load application. Please try again later.';
            console.error(err);
        } finally {
            loading = false;
        }
    };
</script>

<main>
    {#if loading}
        <p class="text-gray-500">Loading application...</p>
    {:else if errorMessage}
        <p class="text-red-500">{errorMessage}</p>
    {:else if application}
        <div>
            <h1 class="text-2xl font-bold">Заявка {application.id}</h1>
            <p><strong>ФИО:</strong> {application.fullName}</p>
            <p><strong>Контакт:</strong> {application.mobileNumber}</p>
            <p><strong>Категория:</strong> {application.catPas}</p>
            <p><strong>Статус:</strong> {application.status}</p>
            <p><strong>Время:</strong> {application.time3} ➜ {application.time4}</p>
        </div>
    {/if}
</main>