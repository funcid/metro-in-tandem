<script lang="ts" type="module">
    import { onMount } from "svelte";
    import { writable } from "svelte/store";
    import { JWT } from "../../utils/auth";
    import { PUBLIC_API_HOST } from "$env/static/public";
    import { findMetroStationById } from "../../utils/metro";

    // Инициализируем магазин для хранения данных
    let data = writable<AllocationResponse>(null);

    // Функция для получения данных allications
    async function fetchAllocations(): Promise<AllocationResponse> {
        const response = await fetch(
            `${PUBLIC_API_HOST}api/v1/day?&from=1713925800000&to=1713996000000`,
            {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${$JWT}`,
                    "Content-Type": "application/json",
                },
            },
        );
        if (!response.ok) {
            throw new Error("Failed to fetch allocations data");
        }
        return await response.json();
    }

    // Функция, вызываемая при монтировании компонента
    onMount(async () => {
        try {
            const fetchedData = await fetchAllocations();
            fetchedData.allocations.sort((a, b) => a.to.localeCompare(b.to));
            data.set(fetchedData);
        } catch (error) {
            console.error(error);
        }
    });

    function getApplication(id: number): Application | null {
        let fetchedData;
        data.subscribe((value) => (fetchedData = value));

        if (!fetchedData || !fetchedData.applications) {
            return null;
        }
        return fetchedData.applications.find((app) => app.id == id);
    }

    function goApplication(id: number | null) {
        if (!id) {
            return;
        }
        window.location.hash = `/applications/${id}`;
    }

    function format(date: string) {
        return date.split(' ')[1].slice(0, 5)
    }
</script>

<main class="flex flex-col gap-[40rem]">
    <p class="font-bold text-[40rem] text-center">Мое расписание</p>
    {#if $data && $data.allocations}
        <div class="flex flex-col gap-[40rem] m-auto text-center">
            <p>
                {$data.employee.rank} / {$data.employee.fio}
            </p>
            <p>
                Смена {$data.employee.timeWork}
            </p>
            <hr />
            {#each $data.allocations as alloc}
                {#if alloc.from != alloc.to}
                    <div>
                        <div class="mb-[10rem]">
                            С {format(alloc.from)}
                            до
                            {format(alloc.to)}
                        </div>
                        <div
                            on:click={() => goApplication(alloc.applicationId)}
                            class="text-left text-[34rem] *:p-[16rem]"
                            style="border-radius: 16rem; background-color: #f5f5f5;"
                        >
                            <div>
                                {alloc.type == "APPLICATION"
                                    ? "Сопровождение заявки #" +
                                      alloc.applicationId
                                    : alloc.type == "TRAVEL"
                                      ? "Перемещение к следующей заявке"
                                      : "Обеденный перерыв"}
                            </div>
                            {#if alloc.type == "APPLICATION" && getApplication(alloc.applicationId)}
                                <div class="flex p-[16rem] gap-[16rem]">
                                    <div class="flex gap-[4rem]">
                                        {@html findMetroStationById(
                                            getApplication(alloc.applicationId)
                                                ?.idSt1,
                                        )?.iconHtml}
                                        {@html findMetroStationById(
                                            getApplication(alloc.applicationId)
                                                ?.idSt1,
                                        )?.nameStation}
                                    </div>
                                    ➜
                                    <div class="flex gap-[4rem]">
                                        {@html findMetroStationById(
                                            getApplication(alloc.applicationId)
                                                ?.idSt2,
                                        )?.iconHtml}
                                        {@html findMetroStationById(
                                            getApplication(alloc.applicationId)
                                                ?.idSt2,
                                        )?.nameStation}
                                    </div>
                                </div>
                            {/if}
                        </div>
                    </div>
                {/if}
            {/each}
        </div>
    {/if}
</main>
