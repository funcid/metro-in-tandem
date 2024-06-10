<script lang="ts">
    import { writable } from 'svelte/store';

    export let JWT = writable<string | null>(null);
    
    let username: string = '';
    let password: string = '';
    let errorMessage: string = '';

    async function login() {
        try {
            const response = await fetch('http://localhost:8080/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });
            
            const data = await response.json();
            if (response.ok) {
                JWT.set(data.token);
                errorMessage = '';
            } else {
                errorMessage = 'Ошибка: ' + (data.message || 'Unknown error');
            }
        } catch (error: any) {
            errorMessage = 'Сервер временно недоступен: ' + error.message;
        }
    }
</script>

<div class="login-form">
    <h1>Аутентификация</h1>
    <form on:submit|preventDefault={login}>
        <label for="username">Логин:</label>
        <input id="username" type="text" bind:value={username} required />
        
        <label for="password">Пароль:</label>
        <input id="password" type="password" bind:value={password} required />
        
        <button type="submit">Войти</button>
    </form>
    {#if errorMessage}
        <p class="error">{errorMessage}</p>
    {/if}
</div>