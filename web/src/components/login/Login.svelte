<script lang="ts">
    import { PUBLIC_API_HOST } from "$env/static/public";
    import { onMount } from "svelte";
    import { login, isAuthenticated } from "../../utils/auth";
    

    let inputUsername: string = "";
    let password: string = "";
    let errorMessage: string = "";
    // onMount(async () => {
    //     if (await checkAuth()) {
    //         window.location.href = "/";
    //     }
    // })

    async function handleLogin() {
        const result = await login(inputUsername, password, PUBLIC_API_HOST);
        if (!result.success) {
            errorMessage = "Ошибка: " + (result.message || "Unknown error");
        } else {
            errorMessage = "";
            document.location.reload()
        }
    }
</script>

<div
    class="flex flex-col gap-[50rem] text-center items-center mt-[-48rem] lg:mt-[0]"
>
    <p class="font-extrabold text-[54rem] lg:text-[32rem]">Вход в систему</p>

    <form
        on:submit|preventDefault={handleLogin}
        class="flex flex-col gap-[30rem] text-left w-[750rem] lg:w-[600rem] *:rounded-[12rem]"
    >
        <div class="flex flex-col gap-[5rem] text-[44rem] lg:text-[32rem]">
            <label for="username" class="flex gap-[6rem]">
                Логин <p class="text-[red]">*</p>
            </label>
            <input
                id="username"
                type="text"
                placeholder="Введите логин"
                bind:value={inputUsername}
                required
                class="border-[1rem] border-gray-700 rounded-[12rem] p-[12rem] lg:p-[16rem]"
            />
        </div>

        <div class="flex flex-col gap-[5rem] text-[44rem] lg:text-[32rem]">
            <label for="password" class="flex gap-[6rem]">
                Пароль <p class="text-[red]">*</p>
            </label>
            <input
                id="password"
                type="password"
                placeholder="Введите пароль"
                bind:value={password}
                required
                class="border-[1rem] border-gray-700 rounded-[12rem] p-[12rem] lg:p-[16rem]"
            />
        </div>

        <button
            type="submit"
            class="bg-[#D4212D] min-h-[88rem] text-[48rem] lg:text-[32rem] text-white border-none p-2 cursor-pointer hover:bg-red-700 p-[32rem] lg:py-[15rem] px-[26rem]"
        >
            Войти
        </button>
    </form>

    {#if errorMessage}
        <p class="text-red-600">{errorMessage}</p>
    {/if}
</div>
