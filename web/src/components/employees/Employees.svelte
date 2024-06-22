<script lang="ts">
    import { PUBLIC_API_HOST } from "$env/static/public";
    import { JWT } from "../../utils/auth";
    import { sections } from "../Variables";

    let selectedSection = sections[0];
    let employees: EmployeeResponse[] = [];
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

    const handleClick = (id: string) => {
        window.location.hash = `/employees/${id}`;
    };
</script>

<main class="flex flex-col gap-[40rem]">
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
        <div>
            <div class="grid grid-cols-[1fr,2fr,1fr,1fr,1fr,1fr] text-gray-600 mb-[30rem]">
                <div>ID</div>
                <div>ФИО</div>
                <div>Время работы</div>
                <div>Должность</div>
                <div>Смена</div>
                <div>Легкий труд</div>
            </div>
            <hr />
            <ul class="list-none p-0">
                {#each employees as employee}
                    <li
                        class="grid grid-cols-[1fr,2fr,1fr,1fr,1fr,1fr] gap-[4rem] py-[32rem] hover:bg-gray-100 cursor-pointer text-[16rem] md:text-[26rem] items-center"
                        on:click={() => handleClick(employee.id)}
                    >
                        <div class="text-blue-600 underline hover:text-blue-800">{employee.id}</div>
                        <div class="text-blue-600 underline hover:text-blue-800">{employee.fio}</div>
                        <div>{employee.timeWork}</div>
                        <div>{employee.rank}</div>
                        <div>{employee.smena}</div>
                        <div>Нет</div>
                    </li>
                {/each}
            </ul>
        </div>
    {/if}
</main>
