<script lang="ts">
    import { JWT } from '../login/Login.svelte'; 

    let fullName = '';
    let contactNumber = '';
    let contactDescription = '';
    let gender = '';
    let category = '';
    let additionalInfo = '';
    let hasPacemaker = false;
    let errorMessage = '';

    const categories = [
        { value: "IZ", label: "ИЗ (Инвалид по зрению, тотальный)" },
        { value: "IZT", label: "ИЗТ (Инвалид по зрению, слабовидящий)" },
        { value: "IS", label: "ИС (Инвалид по слуху)" },
        { value: "IK", label: "ИК (Инвалид колясочник)" },
        { value: "IO", label: "ИО (Инвалид опорник)" },
        { value: "DI", label: "ДИ (Ребенок инвалид)" },
        { value: "PL", label: "ПЛ (Пожилой человек)" },
        { value: "RD", label: "РД (Родители с детьми)" },
        { value: "RDK", label: "РДК (Родители с детскими колясками)" },
        { value: "OGD", label: "ОГД (Организованные группы детей)" },
        { value: "OV", label: "ОВ (Временно маломобильные)" },
        { value: "IU", label: "ИУ (Люди с ментальной инвалидностью)" }
    ];
    
    const handleSubmit = async (event: Event) => {
        event.preventDefault();

        const newPassenger = {
            fullName,
            contactNumbers: [{ number: contactNumber, description: contactDescription }],
            gender,
            category,
            additionalInfo,
            hasPacemaker
        };

        try {
            const response = await fetch('http://localhost:8080/api/v1/passengers', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${$JWT}`, // Используем JWT из store
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newPassenger)
            });

            if (response.ok) {
                const createdPassenger = await response.json();
                window.location.hash = `/passenagers/${createdPassenger.id}`;
            } else {
                errorMessage = 'Ошибка при создании пассажира. Пожалуйста, попробуйте снова.';
            }
        } catch (error) {
            console.error('Error:', error);
            errorMessage = 'Ошибка при создании пассажира. Пожалуйста, попробуйте снова.';
        }
    };
</script>

<main>
    <form class="flex flex-col items-center" on:submit={handleSubmit}>
        <div class="flex flex-col gap-[16rem]">
            <h2 class="font-bold mb-12 text-center text-[36rem]">Создать нового пассажира</h2>
        
            {#if errorMessage}
                <p class="text-red-500 mb-4">{errorMessage}</p>
            {/if}
    
            <div class="mb-4">
                <label class="block text-gray-700 mb-2" for="fullName">ФИО</label>
                <input type="text" id="fullName" bind:value={fullName} class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required />
            </div>
            <div class="mb-4">
                <select id="category" bind:value={category} class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                    <option value="" disabled selected>⏵ Категория</option>
                    {#each categories as { value, label }}
                        <option value={value}>{label}</option>
                    {/each}
                </select>
            </div>

            <div class="mb-4">
                <select id="gender" bind:value={gender} class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                    <option value="" disabled selected>⏵ Пол</option>
                    <option value="Мужчина">Мужчина</option>
                    <option value="Женщина">Женщина</option>
                </select>
            </div>
    
            <div class="mb-4">
                <label class="block text-gray-700 mb-2" for="contactNumber">Контактный номер</label>
                <input type="text" id="contactNumber" bind:value={contactNumber} class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required />
            </div>
    
            <div class="mb-4">
                <label class="block text-gray-700 mb-2" for="additionalInfo">Дополнительная информация</label>
                <textarea id="additionalInfo" bind:value={additionalInfo} class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"></textarea>
            </div>
    
            <div class="mb-4 flex items-center">
                <input type="checkbox" id="hasPacemaker" bind:checked={hasPacemaker} class="mr-2 leading-tight" />
                <label class="text-gray-700 p-[12rem]" for="hasPacemaker">Наличие кардиостимулятора</label>
            </div>
    
            <div class="mb-6">
                <button type="submit" class="bg-[#D4212D] py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full">Создать пассажира</button>
            </div>
        </div>
    </form>
</main>