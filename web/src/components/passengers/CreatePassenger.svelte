<script lang="ts">
    import { PUBLIC_API_HOST } from "$env/static/public";
    import validateRussianPhoneNumber from "../../utils/validatePhoneNumber";
    import { JWT } from "../login/Login.svelte";

    let fullName = "";
    let contactNumber = "";
    let contactDescription = "";
    let gender = "";
    let category = "";
    let additionalInfo = "";
    let hasPacemaker = false;
    let errorMessage = "";

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
        { value: "IU", label: "ИУ (Люди с ментальной инвалидностью)" },
    ];

    const handleSubmit = async (event: Event) => {
        event.preventDefault();

        if (!validateRussianPhoneNumber(contactNumber)) {
            errorMessage = "Неверный формат контактного номера";
            return;
        }
        
        const newPassenger = {
            fullName,
            contactNumbers: [
                { number: contactNumber, description: contactDescription },
            ],
            gender,
            category,
            additionalInfo,
            hasPacemaker,
        };

        try {
            const response = await fetch(
                PUBLIC_API_HOST + `api/v1/passengers`,
                {
                    method: "POST",
                    headers: {
                        Authorization: `Bearer ${$JWT}`, // Используем JWT из store
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(newPassenger),
                },
            );

            if (response.ok) {
                const createdPassenger = await response.json();
                window.location.hash = `/passengers/${createdPassenger.id}`;
            } else {
                errorMessage =
                    "Ошибка при создании пассажира. Пожалуйста, попробуйте снова.";
            }
        } catch (error) {
            console.error("Error:", error);
            errorMessage =
                "Ошибка при создании пассажира. Пожалуйста, попробуйте снова.";
        }
    };
</script>

<main class="flex flex-col justify-center items-center">
    <p class="font-bold text-[40rem] mb-[20rem]">Создать нового пассажира</p>
    <div
        class="flex justify-center bg-white border border-gray-300 shadow-md w-2/5 rounded-[30rem]"
    >
        <form
            class="flex flex-col items-center p-[40rem]"
            on:submit={handleSubmit}
        >
            <div class="flex flex-col gap-[16rem]">
                {#if errorMessage}
                    <p class="text-red-500 mb-4">{errorMessage}</p>
                {/if}

                <div class="mb-4">
                    <label class="block text-gray-700 mb-2" for="fullName"
                        >ФИО</label
                    >
                    <input
                        type="text"
                        id="fullName"
                        bind:value={fullName}
                        class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                        required
                    />
                </div>
                <div class="mb-4">
                    <select
                        id="category"
                        bind:value={category}
                        class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                        required
                    >
                        <option value="" disabled selected>⏵ Категория</option>
                        {#each categories as { value, label }}
                            <option {value}>{label}</option>
                        {/each}
                    </select>
                </div>

                <div class="mb-4">
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

                <div class="mb-4">
                    <label class="block text-gray-700 mb-2" for="contactNumber"
                        >Контактный номер</label
                    >
                    <input
                        type="text"
                        id="contactNumber"
                        bind:value={contactNumber}
                        class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                        required
                    />
                </div>

                <div class="mb-4">
                    <label
                        class="block text-gray-700 mb-2"
                        for="contactDescription">Описание контакта</label
                    >
                    <input
                        type="text"
                        id="contactDescription"
                        bind:value={contactDescription}
                        class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                    />
                </div>

                <div class="mb-4">
                    <label class="block text-gray-700 mb-2" for="additionalInfo"
                        >Дополнительная информация</label
                    >
                    <textarea
                        id="additionalInfo"
                        bind:value={additionalInfo}
                        class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                    ></textarea>
                </div>

                <div class="mb-4 flex items-center">
                    <input
                        type="checkbox"
                        id="hasPacemaker"
                        bind:checked={hasPacemaker}
                        class="mr-2 leading-tight"
                    />
                    <label class="text-gray-700 p-[12rem]" for="hasPacemaker"
                        >Наличие кардиостимулятора</label
                    >
                </div>

                <div class="mb-6">
                    <button
                        type="submit"
                        class="bg-[#D4212D] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full"
                        >Создать пассажира</button
                    >
                </div>
            </div>
        </form>
    </div>
</main>
