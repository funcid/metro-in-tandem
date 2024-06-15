<script lang="ts">
    import { PUBLIC_API_HOST } from "$env/static/public";
    import validateRussianPhoneNumber from "../../utils/validatePhoneNumber";
    import { JWT } from "../login/Login.svelte";
    import {
        workTimes,
        sections,
        shifts,
        positions,
    } from "../Variables.svelte";

    let fullName = "";
    let uchastok = "";
    let gender = "";
    let shift = "";
    let position = "";
    let workTime = "";
    let workPhone = "";
    let personalPhone = "";
    let employeeID = "";
    let lightDuty = false;
    let errorMessage = "";

    const handleSubmit = async (event: Event) => {
        event.preventDefault();

        if (!validateRussianPhoneNumber(workPhone)) {
            errorMessage = "Неверный формат рабочего телефона";
            return;
        }

        if (!validateRussianPhoneNumber(personalPhone)) {
            errorMessage = "Неверный формат личного телефона";
            return;
        }

        let fio = fullName.split(" ");
        const newEmployee = {
            fullName,
            initials: fio[0] + " " + fio[1][0] + ". " + fio[2][0] + ".",
            gender,
            uchastok,
            shift: shift.replace("(", " ").replace(")", " "),
            position,
            workTime,
            workPhone,
            personalPhone,
            employeeID,
            lightDuty,
        };

        try {
            const response = await fetch(PUBLIC_API_HOST + `api/v1/employees`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${$JWT}`, // Используем JWT из store
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(newEmployee),
            });

            if (response.ok) {
                const createdEmployee = await response.json();
                window.location.hash = `/employees/${createdEmployee.id}`;
            } else {
                errorMessage =
                    "Ошибка при создании сотрудника. Пожалуйста, попробуйте снова.";
            }
        } catch (error) {
            console.error("Error:", error);
            errorMessage =
                "Ошибка при создании сотрудника. Пожалуйста, попробуйте снова.";
        }
    };
</script>

<main class="flex flex-col justify-center items-center">
    <p class="font-bold text-[40rem] mb-[20rem]">Создать нового сотрудника</p>
    <div
        class="flex w-full lg:w-[auto] justify-center bg-white border border-gray-300 shadow-md w-3/5 rounded-[30rem]"
    >
        <form on:submit={handleSubmit}>
            <div
                class="flex flex-col items-center gap-[16rem] w-full p-[40rem]"
            >
                {#if errorMessage}
                    <p class="text-red-500 mb-4">{errorMessage}</p>
                {/if}

                <div
                    class="*:flex *:flex-wrap grid grid-cols-1 gap-x-[32rem] *:content-end md:grid-cols-2 *:m-[5rem] mb-[16rem]"
                >
                    <div>
                        <label class="block text-gray-700 mb-2" for="fullName"
                            >ФИО полностью</label
                        >
                        <input
                            type="text"
                            id="fullName"
                            bind:value={fullName}
                            class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            required
                        />
                    </div>

                    <div>
                        <select
                            id="gender"
                            bind:value={gender}
                            class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            required
                        >
                            <option value="" disabled selected>⏵ Пол</option>
                            <option value="Мужчина">Мужчина</option>
                            <option value="Женщина">Женщина</option>
                        </select>
                    </div>

                    <div>
                        <label class="block text-gray-700 mb-2" for="workPhone"
                            >Рабочий телефон</label
                        >
                        <input
                            type="text"
                            id="workPhone"
                            bind:value={workPhone}
                            class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            required
                        />
                    </div>

                    <div>
                        <select
                            id="position"
                            bind:value={position}
                            class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            required
                        >
                            <option value="" disabled selected
                                >⏵ Должность сотрудника</option
                            >
                            {#each positions as positionOption}
                                <option value={positionOption}
                                    >{positionOption}</option
                                >
                            {/each}
                        </select>
                    </div>

                    <div>
                        <label
                            class="block text-gray-700 mb-2"
                            for="personalPhone">Личный телефон</label
                        >
                        <input
                            type="text"
                            id="personalPhone"
                            bind:value={personalPhone}
                            class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            required
                        />
                    </div>

                    <div>
                        <select
                            id="workTime"
                            bind:value={workTime}
                            class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            required
                        >
                            <option value="" disabled selected
                                >⏵ Время работы</option
                            >
                            {#each workTimes as workTimeOption}
                                <option value={workTimeOption}
                                    >{workTimeOption}</option
                                >
                            {/each}
                        </select>
                    </div>

                    <div>
                        <label class="block text-gray-700 mb-2" for="employeeID"
                            >Табельный номер</label
                        >
                        <input
                            type="text"
                            id="employeeID"
                            bind:value={employeeID}
                            class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            required
                        />
                    </div>

                    <div>
                        <select
                            id="shift"
                            bind:value={shift}
                            class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            required
                        >
                            <option value="" disabled selected
                                >⏵ Смена сотрудника</option
                            >
                            {#each shifts as shiftOption}
                                <option value={shiftOption}
                                    >{shiftOption}</option
                                >
                            {/each}
                        </select>
                    </div>

                    <div class="flex h-full items-center pt-[20rem]">
                        <input
                            type="checkbox"
                            id="lightDuty"
                            bind:checked={lightDuty}
                            class="mr-2 leading-tight w-[32rem] h-[32rem]"
                        />
                        <label class="text-gray-700 p-[12rem]" for="lightDuty"
                            >Легкий труд</label
                        >
                    </div>

                    <div>
                        <select
                            id="uchastok"
                            bind:value={uchastok}
                            class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            required
                        >
                            <option value="" disabled selected>⏵ Участок</option
                            >
                            {#each sections as sectionOption}
                                <option value={sectionOption}
                                    >{sectionOption}</option
                                >
                            {/each}
                        </select>
                    </div>
                </div>

                <div class="w-2/4">
                    <button
                        type="submit"
                        class="bg-[#D4212D] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full"
                        >Создать сотрудника</button
                    >
                </div>
            </div>
        </form>
    </div>
</main>
