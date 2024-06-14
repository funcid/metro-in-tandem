<script lang="ts">
    import { onMount } from "svelte";
    import { JWT } from "../login/Login.svelte";
    import { PUBLIC_API_HOST } from "$env/static/public";
    import { categories } from "../Variables.svelte";

    let passenger: PassengerDetailResponse | null = null;
    let loading: boolean = true;
    let errorMessage: string = "";

    const getIdFromUrl = () => {
        const url = window.location.hash;
        const parts = url.split("/");
        return parts[2]; // Третья часть URL содержит id
    };

    const id = getIdFromUrl();

    onMount(async () => {
        await fetchPassenger();
    });

    const handleClick = () => {
        window.location.hash = `/create-application/${id}`;
    };

    const fetchPassenger = async () => {
        loading = true;
        try {
            const response = await fetch(
                PUBLIC_API_HOST + `api/v1/passengers/${id}`,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${$JWT}`,
                        "Content-Type": "application/json",
                    },
                },
            );
            if (!response.ok) {
                throw new Error("Failed to fetch passenger");
            }
            passenger = await response.json();
        } catch (err) {
            errorMessage = "Failed to load passenger. Please try again later.";
            console.error(err);
        } finally {
            loading = false;
        }
    };

    const updatePassenger = async () => {
        try {
            const response = await fetch(
                PUBLIC_API_HOST + `api/v1/passengers/${id}`,
                {
                    method: "PUT",
                    headers: {
                        Authorization: `Bearer ${$JWT}`,
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(passenger),
                },
            );
            if (!response.ok) {
                throw new Error("Failed to update passenger");
            }
            window.location.hash = "/passengers"; // обновление Hash
        } catch (err) {
            errorMessage =
                "Failed to update passenger. Please try again later.";
            console.error(err);
        }
    };

    const deletePassenger = async () => {
        try {
            const response = await fetch(
                PUBLIC_API_HOST + `api/v1/passengers/${id}`,
                {
                    method: "DELETE",
                    headers: {
                        Authorization: `Bearer ${$JWT}`,
                        "Content-Type": "application/json",
                    },
                },
            );
            if (!response.ok) {
                throw new Error("Failed to delete passenger");
            }
            window.location.hash = "/passengers"; // обновление Hash
        } catch (err) {
            errorMessage =
                "Failed to delete passenger. Please try again later.";
            console.error(err);
        }
    };
</script>

<main class="flex flex-col justify-center items-center">
    <p class="font-bold text-[40rem] mb-[20rem]">
        Просмотр/редактирование пассажира
    </p>
    <div
        class="flex justify-center bg-white border border-gray-300 shadow-md w-2/5 rounded-[30rem]"
    >
        {#if loading}
            <p class="text-gray-500">Loading passenger...</p>
        {:else if errorMessage}
            <p class="text-red-500">{errorMessage}</p>
        {:else if passenger}
            <div class="flex justify-between p-[40rem] gap-[40rem] w-full">
                <form
                    class="flex flex-col gap-[12rem] w-full"
                    on:submit|preventDefault={updatePassenger}
                >
                    <div class="mb-4">
                        <label class="block text-gray-700">Полное имя</label>
                        <input
                            type="text"
                            bind:value={passenger.fullName}
                            class="shadow appearance-none border rounded-[12rem] p-[12rem] w-full text-gray-700"
                        />
                    </div>
                    <div class="mb-4">
                        <select
                            bind:value={passenger.gender}
                            class="shadow appearance-none border rounded-[12rem] p-[12rem] w-full text-gray-700"
                        >
                            <option value="Мужчина">Мужчина</option>
                            <option value="Женщина">Женщина</option>
                        </select>
                    </div>
                    <div class="mb-4">
                        <select
                            id="category"
                            bind:value={passenger.category}
                            class="shadow appearance-none border rounded-[12rem] w-full p-[12rem] text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            required
                        >
                            <option value="" disabled selected
                                >⏵ Категория</option
                            >
                            {#each categories as { value, label }}
                                <option {value}>{label}</option>
                            {/each}
                        </select>
                    </div>
                    <div class="mb-4">
                        <label class="block text-gray-700"
                            >Дополнительная информация</label
                        >
                        <textarea
                            bind:value={passenger.additionalInfo}
                            class="shadow appearance-none border rounded-[12rem] p-[12rem] w-full text-gray-700"
                        />
                    </div>
                    <div>
                        <label class="block text-gray-700"
                            >Контактные номера:</label
                        >
                        <div class="flex flex-col gap-[12rem]">
                            {#each passenger.contactNumbers as contact, index}
                                <div class="flex flex-col">
                                    <div class="text-gray-700">
                                        {index + 1}:
                                        <input
                                            type="text"
                                            bind:value={contact.number}
                                            class="mb-[20rem] shadow appearance-none border rounded-[12rem] p-[12rem] text-gray-700"
                                        />
                                    </div>
                                </div>
                            {/each}
                        </div>
                    </div>
                    <div class="flex">
                        <label class="block text-gray-700"
                            >Имеет кардиостимулятор</label
                        >
                        <input
                            type="checkbox"
                            id="hasPacemaker"
                            bind:checked={passenger.hasPacemaker}
                            class="ml-[12rem] leading-tight w-[32rem] h-[32rem]"
                        />
                    </div>
                    <button
                        type="button"
                        on:click={deletePassenger}
                        class="bg-[#D4212D] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full"
                    >
                        Удалить пассажира
                    </button>
                    <button
                        type="submit"
                        class="bg-blue-500 hover:bg-blue-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full"
                    >
                        Сохранить изменения
                    </button>
                    <button
                        type="button"
                        on:click={() => handleClick()}
                        class="bg-green-500 hover:bg-green-700 py-[12rem] px-[26rem] rounded-[12rem] items-center text-white w-full mt-4"
                    >
                        Создать заявку на сопровождение
                    </button>
                </form>
            </div>
        {/if}
    </div>
</main>
