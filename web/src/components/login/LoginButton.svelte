<script lang="ts">
    import { JWT, username, role } from "../../utils/auth";
    import Login from "$lib/images/login.svg";
    
    export let toggleLogoutModal;
</script>

<div class="lg:w-fit w-full">
    {#if $username}
        <div
            class="invisible lg:visible h-0 lg:h-full flex items-center gap-[10rem] cursor-pointer hover:bg-gray-100 rounded-[20rem] p-[20rem]"
            on:click={toggleLogoutModal}
        >
            <div class="text-[34rem] flex flex-col">
                <p>{$username}</p>
                <p class="text-[20rem] text-red-700 font-bold">
                    {$role}
                </p>
            </div>

            <div
                class="w-[60rem] h-[60rem] rounded-full bg-gradient-to-r from-blue-500 to-purple-500"
            ></div>
        </div>
    {:else}
        <a class="text-white" href="#/login">
            <button
                class="text-[96rem] lg:text-[27rem] flex gap-[20rem] bg-[#D4212D] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[12rem] items-center"
            >
                Войти
                <img src={Login} alt="#" width="27rem" height="27rem" />
            </button>
        </a>
    {/if}
</div>
