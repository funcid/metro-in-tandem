<script lang="ts">
    import { onMount } from "svelte";
    import { JWT } from "../login/Login.svelte";
    import { PUBLIC_API_HOST } from "$env/static/public";
    import flatpickr from "flatpickr";
    import { workTimes, sections, shifts, positions } from "../Variables";

    let employee: EmployeeDetailResponse | null = null;
    let loading: boolean = true;
    let errorMessage: string = "";
    let selectedStatus: string = "vacation"; // Default status
    let updatedDates: string[] = []; // Array to hold selected dates

    const getIdFromUrl = () => {
        const url = window.location.hash;
        const parts = url.split("/");
        return parts[2]; // Third part of the URL contains the id
    };

    const id = getIdFromUrl();

    onMount(async () => {
        await fetchEmployee();
        initCalendar();
    });

    const fetchEmployee = async () => {
        loading = true;
        try {
            const response = await fetch(
                PUBLIC_API_HOST + `api/v1/employees/${id}`,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${$JWT}`,
                        "Content-Type": "application/json",
                    },
                },
            );
            if (!response.ok) {
                throw new Error("Failed to fetch application");
            }
            employee = await response.json();
        } catch (err) {
            errorMessage =
                "Failed to load application. Please try again later.";
            console.error(err);
        } finally {
            loading = false;
        }
    };

    const updateEmployee = async () => {
        // Update employee object with new dates based on selectedStatus
        if (selectedStatus === "vacation") {
            employee!.vacations = [...new Set([...employee!.vacations!, ...updatedDates])];
        } else if (selectedStatus === "sick-leave") {
            employee!.sickLeaves = [...new Set([...employee!.sickLeaves!, ...updatedDates])];
        } else if (selectedStatus === "day-off") {
            employee!.daysOff = [...new Set([...employee!.daysOff!, ...updatedDates])];
        } else if (selectedStatus === "work-day") {
            employee!.vacations = employee!.vacations.filter(date => !updatedDates.includes(date));
            employee!.sickLeaves = employee!.sickLeaves.filter(date => !updatedDates.includes(date));
            employee!.daysOff = employee!.daysOff.filter(date => !updatedDates.includes(date));
        }

        try {
            const response = await fetch(
                PUBLIC_API_HOST + `api/v1/employees/${id}`,
                {
                    method: "PUT",
                    headers: {
                        Authorization: `Bearer ${$JWT}`,
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(employee),
                },
            );
            if (!response.ok) {
                throw new Error("Failed to update application");
            }
            window.location.hash = `/employees`;
        } catch (err) {
            errorMessage =
                "Failed to update application. Please try again later.";
            console.error(err);
        }
    };

    const deleteEmployee = async () => {
        try {
            const response = await fetch(
                PUBLIC_API_HOST + `api/v1/employees/${id}`,
                {
                    method: "DELETE",
                    headers: {
                        Authorization: `Bearer ${$JWT}`,
                        "Content-Type": "application/json",
                    },
                },
            );
            if (!response.ok) {
                throw new Error("Failed to delete application");
            }
            window.location.hash = `/employees`;
        } catch (err) {
            errorMessage =
                "Failed to delete application. Please try again later.";
            console.error(err);
        }
    };

    const initCalendar = () => {
        flatpickr("#employee-calendar", {
            mode: "multiple",
            dateFormat: "Y-m-d",
            inline: true,
            onDayCreate: (dObj, dStr, fp, dayElem) => {
                const date = dayElem.dateObj.toISOString().split("T")[0];
                if (employee?.vacations?.includes(date)) {
                    dayElem.classList.add("vacation");
                }
                if (employee?.sickLeaves?.includes(date)) {
                    dayElem.classList.add("sick-leave");
                }
                if (employee?.daysOff?.includes(date)) {
                    dayElem.classList.add("day-off");
                }
            },
            onChange: (selectedDates, dateStr, instance) => {
                updatedDates = selectedDates.map(
                    (date) => date.toISOString().split("T")[0],
                );

                // Update UI immediately for selected dates
                instance.days.forEach(dayElem => {
                    const date = dayElem.dateObj.toISOString().split("T")[0];
                    if (updatedDates.includes(date)) {
                        if (selectedStatus === "vacation") {
                            dayElem.classList.add("vacation");
                            dayElem.classList.remove("sick-leave", "day-off", "work-day");
                        } else if (selectedStatus === "sick-leave") {
                            dayElem.classList.add("sick-leave");
                            dayElem.classList.remove("vacation", "day-off", "work-day");
                        } else if (selectedStatus === "day-off") {
                            dayElem.classList.add("day-off");
                            dayElem.classList.remove("vacation", "sick-leave", "work-day");
                        } else if (selectedStatus === "work-day") {
                            dayElem.classList.remove("vacation", "sick-leave", "day-off");
                            dayElem.classList.add("work-day");
                        }
                    }
                });
            },
        });
    };
</script>

<main class="flex flex-col justify-center items-center">
    <p class="font-bold text-[40rem] mb-[20rem]">
        Просмотр/редактирование сотрудника {id}
    </p>
    <div class="flex justify-center gap-[40rem]">
        <div class="flex w-full lg:w-[auto] justify-center bg-white border border-gray-300 shadow-md rounded-[30rem]">
            {#if loading}
                <p class="text-gray-500">Loading application...</p>
            {:else if errorMessage}
                <p class="text-red-500">{errorMessage}</p>
            {:else if employee}
                <div class="flex justify-between p-[40rem] gap-[40rem]">
                    <form class="flex flex-col justify-between" on:submit|preventDefault={updateEmployee}>
                        <div class="flex flex-col gap-[12rem]">
                            <p class="font-bold">{employee.fio}</p>
                            <div>
                                <label class="block text-gray-700">Участок</label>
                                <select bind:value={employee.uchastok} class="shadow appearance-none border rounded-[12rem] p-[12rem] w-full text-gray-700">
                                    {#each sections as section}
                                        <option value={section}>{section}</option>
                                    {/each}
                                </select>
                            </div>
                            <div class="mb-4">
                                <label class="block text-gray-700">Рабочее время</label>
                                <select bind:value={employee.timeWork} class="shadow appearance-none border rounded-[12rem] p-[12rem] w-full text-gray-700">
                                    {#each workTimes as workTime}
                                        <option value={workTime}>{workTime}</option>
                                    {/each}
                                </select>
                            </div>
                            <div class="mb-4">
                                <label class="block text-gray-700">Смена</label>
                                <select bind:value={employee.smena} class="shadow appearance-none border rounded-[12rem] p-[12rem] w-full text-gray-700">
                                    {#each shifts as smena}
                                        <option value={smena}>{smena}</option>
                                    {/each}
                                </select>
                            </div>
                            <div>
                                <label class="block text-gray-700">Пол</label>
                                <select bind:value={employee.sex} class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                                    <option value="Мужской">Мужской</option>
                                    <option value="Женский">Женский</option>
                                </select>
                            </div>
                            <div>
                                <label class="block text-gray-700">Должность</label>
                                <select bind:value={employee.rank} class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                                    {#each positions as rank}
                                        <option value={rank}>{rank}</option>
                                    {/each}
                                </select>
                            </div>
                        </div>
                        <div class="flex gap-[30rem] mt-[30rem]">
                            <button type="submit" class="bg-blue-500 min-h-[88rem] hover:bg-blue-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full">
                                Сохранить
                            </button>
                            <button type="button" on:click={deleteEmployee} class="bg-[#D4212D] min-h-[88rem] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full">
                                Удалить
                            </button>
                        </div>
                    </form>
                </div>
            {/if}
        </div>
        <div class="flex lg:w-[auto] justify-center bg-white border border-gray-300 shadow-md rounded-[30rem]">
            {#if loading}
                <p class="text-gray-500">Loading application...</p>
            {:else if errorMessage}
                <p class="text-red-500">{errorMessage}</p>
            {:else if employee}
                <div class="flex justify-between p-[40rem] gap-[40rem]">
                    <div class="flex flex-col justify-between gap-[20rem]">
                        <p class="font-bold">Корпоративный календарь</p>
                        <div class="mb-[20rem] w-fit">
                            <div id="employee-calendar"></div>
                        </div>
                        <div class="flex gap-[30rem]">
                            <button type="button" on:click={updateEmployee} class="bg-blue-500 min-h-[88rem] hover:bg-blue-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full">
                                Сохранить
                            </button>
                            <select bind:value={selectedStatus} class="min-h-[88rem] shadow appearance-none border rounded-[12rem] p-[12rem] w-full text-center text-gray-700">
                                <option value="vacation">Отпуск</option>
                                <option value="sick-leave">Больничный</option>
                                <option value="day-off">Выходной</option>
                                <option value="work-day">Рабочий день</option>
                            </select>
                        </div>
                    </div>
                </div>
            {/if}
        </div>
    </div>
</main>
