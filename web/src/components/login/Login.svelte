<script context="module" lang="ts">
    import { writable, derived } from 'svelte/store';

    export let JWT = writable<string | null>(null);
    export let username = writable<string | null>(null); 
    export let isAuthenticated = derived(JWT, ($JWT) => !!$JWT);

    isAuthenticated.subscribe((auth) => {
        if (auth) {
            window.location.href = '/#/';  // Перенаправление на корневой путь
        }
    });

    let inputUsername: string = '';
    let password: string = '';
    let errorMessage: string = '';

    async function login() {
        try {
            const response = await fetch('http://localhost:8080/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username: inputUsername, password })
            });

            const data = await response.json();
            if (response.ok) {
                JWT.set(data.token);
                username.set(inputUsername); // сохранить имя пользователя
                errorMessage = '';
            } else {
                errorMessage = 'Ошибка: ' + (data.message || 'Unknown error');
            }
        } catch (error: any) {
            errorMessage = 'Проверьте почту и пароль';
        }
    }
</script>

<div class="flex flex-col gap-[50rem] text-center items-center">
    <p class="font-extrabold">Аутентификация</p>
    <form 
        on:submit|preventDefault={login}
        class="flex flex-col gap-[30rem] text-left w-[600rem] *:rounded-[12rem]"
    >
        <div class="flex flex-col gap-[5rem]">
            <label for="username">Логин</label>
            <input id="username" type="text" bind:value={inputUsername} required 
                   class="border-[1rem] border-gray-700 rounded-[12rem] p-[12rem]"/>
        </div>
        <div class="flex flex-col gap-[5rem]">
            <label for="password">Пароль</label>
            <input id="password" type="password" bind:value={password} required 
                   class="border-[1rem] border-gray-700 rounded-[12rem] p-[12rem]"/>
        </div>
        <button type="submit" 
                class="bg-[#D4212D] text-white border-none p-2 cursor-pointer hover:bg-red-700 py-[15rem] px-[26rem]">
            Войти
        </button>
    </form>
    {#if errorMessage}
        <p class="text-red-600">{errorMessage}</p>
    {/if}
</div>