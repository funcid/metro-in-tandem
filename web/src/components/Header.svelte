<script lang="ts">
    import { JWT, username, role } from "../utils/auth";
    import ApplicationImage from "$lib/images/home/advice.png";
    import PassengerImage from "$lib/images/home/question.png";
    import EmployeeImage from "$lib/images/home/team.png";
    import DistributionImage from "$lib/images/home/distribution.png";
    import BurgerIcon from "$lib/images/icon/burger.png";
    import Cross from "$lib/images/icon/cross.png";
    import LoginButton from "./login/LoginButton.svelte";

    let showLogoutModal = false;

    function toggleLogoutModal() {
        showLogoutModal = !showLogoutModal;
    }

    function logout() {
        JWT.set(null);
        showLogoutModal = false;
        window.location.href = "/";
    }

    let open = false;
    function toggleMenu() {
        window.scrollTo(0, 0);
        if (open) {
            document.documentElement.style.overflowY = "scroll";
            open = false;
            return;
        }
        document.documentElement.style.overflowY = "hidden";
        open = true;
    }
</script>

<div class="flex flex-col gap-[40rem]">
    <div class="flex justify-between items-center">
        <a href="#/">
            <img
                src="https://apps3proxy.mosmetro.tech/webapp-mosmetro/mm-logo-red.svg"
                alt="#"
                class="w-[500rem] lg:w-[325rem]"
            />
        </a>
        <div class="flex gap-[60rem] items-center">
            <div
                class={`lg:hidden w-[100rem] h-[100rem] absolute top-[103rem] left-[865rem]`}
            >
                {#if open}
                    <img
                        class="scale-[2]"
                        src={Cross}
                        alt="cross"
                        on:click={toggleMenu}
                    />
                {:else}
                    <img
                        class="w-[100rem]"
                        src={BurgerIcon}
                        alt="burger"
                        on:click={toggleMenu}
                    />
                {/if}
            </div>

            <div class="hidden lg:block">
                <LoginButton toggleLogoutModal={toggleLogoutModal}/>
            </div>
        </div>
    </div>
    {#if $role != "Сотрудник"}
        <div class="justify-between hidden lg:flex">
            <a href="#/" class="topic">Главная</a>
            <a href="#/applications" class="topic">Заявки</a>
            <a href="#/passengers" class="topic">Пассажиры</a>
            <a href="#/distribution" class="topic">Распределение</a>
            <a href="#/employees" class="topic">Команда</a>
        </div>
    {/if}

    {#if showLogoutModal}
        <div
            class="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
            on:click={toggleLogoutModal}
        >
            <div
                class="flex flex-col bg-white p-[48rem] lg:p-[32rem] w-full lg:w-[auto] text-center lg:text-left text-[54rem] lg:text-[32rem] rounded-[24rem] p-[24rem] gap-[36rem] lg:gap-[24rem]"
                on:click|stopPropagation
            >
                <p>Вы уверены, что хотите выйти?</p>
                <button
                    class="bg-[#D4212D] hover:bg-red-700 text-white p-[48rem] lg:p-[24rem] rounded-[12rem]"
                    on:click={logout}>Выйти</button
                >
            </div>
        </div>
    {/if}
</div>

{#if open}
    <div
        class={`fixed mt-[150rem] ml-[-100rem] w-screen h-[calc(100vh-240rem)] bg-white flex flex-col items-center pt-[200rem] z-50`}
    >
        <nav
            class="flex flex-col h-[65%] gap-[48rem] justify-between"
            on:click={toggleMenu}
        >
            <LoginButton toggleLogoutModal={toggleLogoutModal}/>
            <div class="flex gap-[80rem] items-center">
                <img class="w-[120rem]" src={ApplicationImage} alt={"заявки"} />
                <a href="#/applications" class="text-[96rem]">Заявки</a>
            </div>

            <div class="flex gap-[80rem] items-center">
                <img class="w-[120rem]" src={PassengerImage} alt={"заявки"} />
                <a href="#/passengers" class="text-[96rem]">Пассажиры</a>
            </div>

            <div class="flex gap-[80rem] items-center">
                <img
                    class="w-[120rem]"
                    src={DistributionImage}
                    alt={"Распределение"}
                />
                <a href="#/distribution" class="text-[96rem]">Распределение</a>
            </div>

            <div class="flex gap-[80rem] items-center">
                <img class="w-[120rem]" src={EmployeeImage} alt={"заявки"} />
                <a href="#/employees" class="text-[96rem]">Команда</a>
            </div>
        </nav>
    </div>
{/if}
