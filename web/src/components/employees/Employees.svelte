<script lang="ts">
    import { PUBLIC_API_HOST } from "$env/static/public";
    import { JWT } from "../login/Login.svelte";
    import { sections } from "../Variables";

    let selectedSection = sections[0];
    let employees: EmployeeResponse[] = []; // Типизация массива сотрудников
    let loading = false;
    let errorMessage = "";
    let noContent = false;

    const fetchEmployeesBySection = async (section: string) => {
        loading = true;
        noContent = false;
        try {
            const response = await fetch(
                PUBLIC_API_HOST + `api/v1/employees?region=${section}`,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${$JWT}`,
                        "Content-Type": "application/json",
                    },
                },
            );
            if (response.status === 204) {
                // No Content
                employees = [];
                noContent = true;
            } else if (!response.ok) {
                throw new Error("Network response was not ok");
            } else {
                employees = await response.json();
            }
        } catch (err) {
            errorMessage = "Failed to load employees. Please try again later.";
            console.error(err);
        } finally {
            loading = false;
        }
    };

    $: if (selectedSection) {
        fetchEmployeesBySection(selectedSection);
    }

    const handleCreateEmployee = () => {
        window.location.hash = "/create-employee";
    };

    // Функция для обработки клика по заявке
    const handleClick = (id: string) => {
        window.location.hash = `/employees/${id}`;
    };
</script>

<main class="flex flex-col gap-[20rem]">
    <p class="font-bold text-[40rem]">Сотрудники</p>
    <div class="flex justify-between">
        <select
            bind:value={selectedSection}
            class="border border-gray-400 p-[20rem] rounded-[20rem]"
        >
            {#each sections as section}
                <option value={section}>{section}</option>
            {/each}
        </select>
        <button
            class="bg-[#D4212D] w-[310rem] h-[82rem] hover:bg-red-700 py-[12rem] px-[26rem] rounded-[20rem] items-center text-white"
            on:click={handleCreateEmployee}
        >
            Новый работник
        </button>
    </div>
    <hr />
    {#if loading}
        <p class="text-gray-500 text-center">Загрузка сотрудников...</p>
    {:else if errorMessage}
        <p class="text-red-500 text-center">{errorMessage}</p>
    {:else if noContent || employees.length === 0}
        <p class="text-gray-500 text-center">Сотрудников не найдено</p>
    {:else}
        <div class="grid grid-cols-1 gap-4 p-4 md:grid-cols-2 lg:grid-cols-2">
            {#each employees as employee}
                <div
                    on:click={() => handleClick(employee.id)}
                    class="bg-white border border-gray-300 shadow-md overflow-hidden cursor-pointer hover:bg-gray-100 rounded-[20rem] p-[20rem] ml-[12rem] mb-[12rem]"
                >
                    <p>
                        {employee.fio} ({employee.rank})
                    </p>
                    <br />
                    <p>Время работы: {employee.timeWork}</p>
                    <p>Смена: {employee.smena}</p>
                </div>
            {/each}
        </div>
    {/if}
</main>
