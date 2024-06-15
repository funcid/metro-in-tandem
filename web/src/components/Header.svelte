<script lang="ts">
    import { JWT, isAuthenticated, username } from "./login/Login.svelte";
    import Login from "$lib/images/login.svg";

    let showLogoutModal = false;

    function toggleLogoutModal() {
        showLogoutModal = !showLogoutModal;
    }

    function logout() {
        JWT.set(null);
        showLogoutModal = false;
        window.location.href = "/";
    }
</script>

<div class="flex flex-col gap-[40rem]">
    <div class="flex justify-between items-center">
        <a href="#/">
            <img
                src="https://apps3proxy.mosmetro.tech/webapp-mosmetro/mm-logo-red.svg"
                alt="#"
                width="221rem"
                height="56.61rem"
            />
        </a>
        <div class="flex gap-[60rem] items-center">
            {#if $isAuthenticated}
                <div
                    class="flex items-center gap-[10rem] cursor-pointer hover:bg-gray-100 rounded-[20rem] p-[20rem]"
                    on:click={toggleLogoutModal}
                >
                    <p>{$username}</p>
                    <div
                        class="w-[60rem] h-[60rem] ml-[10rem] rounded-full bg-gradient-to-r from-blue-500 to-purple-500"
                    ></div>
                    <!-- Gradient circle -->
                </div>
            {:else}
                <a class="text-white text-[27rem]" href="#/login">
                    <button
                        class="flex gap-[20rem] bg-[#D4212D] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[12rem] items-center"
                    >
                        Войти
                        <img src={Login} alt="#" width="27rem" height="27rem" />
                    </button>
                </a>
            {/if}
        </div>
    </div>
    <div class="flex justify-between">
        {#if $isAuthenticated}
            <a href="#/" class="topic">Главная</a>
            <a href="#/applications" class="topic">Заявки</a>
            <a href="#/passengers" class="topic">Пассажиры</a>
            <a href="#/distribution" class="topic">Распределение</a>
            <a href="#/employees" class="topic">Команда</a>
        {:else}
            <a href="#/" class="topic">Главная</a>
            <a href="#/login" class="topic">Заявки</a>
            <a href="#/login" class="topic">Пассажиры</a>
            <a href="#/login" class="topic">Распределение</a>
            <a href="#/login" class="topic">Команда</a>
        {/if}
    </div>

    {#if showLogoutModal}
        <div
            class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
            on:click={toggleLogoutModal}
        >
            <div
                class="flex flex-col bg-white p-5 rounded-[24rem] p-[24rem] gap-[12rem]"
                on:click|stopPropagation
            >
                <p>Вы уверены, что хотите выйти?</p>
                <button
                    class="bg-[#D4212D] hover:bg-red-700 text-white p-[12rem] rounded-[12rem]"
                    on:click={logout}>Выйти</button
                >
            </div>
        </div>
    {/if}
</div>
