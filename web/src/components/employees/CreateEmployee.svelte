<script lang="ts">
    import { JWT } from '../login/Login.svelte'; 
    
    let sections = ["ЦУ-1", "ЦУ-2", "ЦУ-3", "ЦУ-3 (Н)", "ЦУ-4", "ЦУ-4 (Н)", "ЦУ-5", "ЦУ-8"];

    let fullName = '';
    let uchastok = '';
    let gender = '';
    let shift = '';
    let position = '';
    let workTime = '';
    let workPhone = '';
    let personalPhone = '';
    let employeeID = '';
    let lightDuty = false;
    let errorMessage = '';

    const shifts = ["1", "2", "1(Н)", "2(Н)", "5"];
    const positions = ["ЦСИ", "ЦИ"];
    const workTimes = ["07:00-19:00", "08:00-20:00", "20:00-08:00", "08:00-17:00"];
    
    const handleSubmit = async (event: Event) => {
        event.preventDefault();

        let fio = fullName.split(' ')
        const newEmployee = {
            fullName,
            initials: fio[0] + ' ' + fio[1][0] + '. ' + fio[2][0] + '.',
            gender,
            uchastok,
            shift: shift.replace('(', ' ').replace(')', ' '),
            position,
            workTime,
            workPhone,
            personalPhone,
            employeeID,
            lightDuty
        };

        try {
            const response = await fetch('http://localhost:8080/api/v1/employees', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${$JWT}`, // Используем JWT из store
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newEmployee)
            });

            if (response.ok) {
                const createdEmployee = await response.json();
                window.location.hash = `/employees/${createdEmployee.id}`;
            } else {
                errorMessage = 'Ошибка при создании сотрудника. Пожалуйста, попробуйте снова.';
            }
        } catch (error) {
            console.error('Error:', error);
            errorMessage = 'Ошибка при создании сотрудника. Пожалуйста, попробуйте снова.';
        }
    };
</script>

<main>
    <form class="flex flex-col items-center" on:submit={handleSubmit}>
        <div class="flex flex-col items-center gap-[16rem] w-full">

            <h2 class="font-bold mb-12 text-center text-[36rem]">Создать нового сотрудника</h2>
        
            {#if errorMessage}
                <p class="text-red-500 mb-4">{errorMessage}</p>
            {/if}

            <div class="grid grid-cols-1 gap-x-[32rem] *:content-end md:grid-cols-2 w-2/3 *:m-[5rem]">
                
                <div>
                    <label class="block text-gray-700 mb-2" for="fullName">ФИО полностью</label>
                    <input type="text" id="fullName" bind:value={fullName} class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required />
                </div>

                <div>
                    <select id="gender" bind:value={gender} class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                        <option value="" disabled selected>⏵ Пол</option>
                        <option value="Мужчина">Мужчина</option>
                        <option value="Женщина">Женщина</option>
                    </select>
                </div>
                
                <div>
                    <label class="block text-gray-700 mb-2" for="workPhone">Рабочий телефон</label>
                    <input type="text" id="workPhone" bind:value={workPhone} class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required />
                </div>

                <div>
                    <select id="position" bind:value={position} class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                        <option value="" disabled selected>⏵ Должность сотрудника</option>
                        {#each positions as positionOption}
                            <option value={positionOption}>{positionOption}</option>
                        {/each}
                    </select>
                </div>

                <div>
                    <label class="block text-gray-700 mb-2" for="personalPhone">Личный телефон</label>
                    <input type="text" id="personalPhone" bind:value={personalPhone} class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required />
                </div>

                <div>
                    <select id="workTime" bind:value={workTime} class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                        <option value="" disabled selected>⏵ Время работы</option>
                        {#each workTimes as workTimeOption}
                            <option value={workTimeOption}>{workTimeOption}</option>
                        {/each}
                    </select>
                </div>
                
                <div>
                    <label class="block text-gray-700 mb-2" for="employeeID">Табельный номер</label>
                    <input type="text" id="employeeID" bind:value={employeeID} class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required />
                </div>

                <div>
                    <select id="shift" bind:value={shift} class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                        <option value="" disabled selected>⏵ Смена сотрудника</option>
                        {#each shifts as shiftOption}
                            <option value={shiftOption}>{shiftOption}</option>
                        {/each}
                    </select>
                </div>

                <div class="flex h-full items-center pt-[20rem]">
                    <input type="checkbox" id="lightDuty" bind:checked={lightDuty} class="mr-2 leading-tight w-[32rem] h-[32rem]" />
                    <label class="text-gray-700 p-[12rem]" for="lightDuty">Легкий труд</label>
                </div>

                <div>
                    <select id="uchastok" bind:value={uchastok} class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                        <option value="" disabled selected>⏵ Участок</option>
                        {#each sections as sectionOption}
                            <option value={sectionOption}>{sectionOption}</option>
                        {/each}
                    </select>
                </div>
            </div>
            
            <div class="w-1/4">
                <button type="submit" class="bg-[#D4212D] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full">Создать сотрудника</button>
            </div>
        </div>
    </form>
</main>