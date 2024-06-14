<script lang="ts">
    import AimIcon from "$lib/images/icon/black_aim.svg?raw";
    import PlusIcon from "$lib/images/icon/black_plus.svg?raw";

    export let icon: string;
    export let jobName: string;
    export let date: string;
    export let position: string;
    export let actual: boolean = false;
    export let route: string;

    const handleClick = () => {
        window.location.hash = `/${route}`;
    };
</script>

<div
    on:click={handleClick}
    class="lg:min-w-[820rem] min-w-[830rem] h-[200rem] flex gap-[30rem] cursor-pointer hover:bg-gray-100 rounded-[20rem] p-[30rem]"
>
    <img src={icon} alt={jobName} />
    <div class="w-full flex justify-between">
        <div class="flex flex-col h-full justify-between">
            <p class="text-[56rem] font-bold font-oddval">{jobName}</p>
            <p class="font-onest text-[24rem]/[128%]">
                {date}<br />
                {position}
            </p>
        </div>
        {#if actual}
            {@html PlusIcon}
        {:else}
            {@html AimIcon}
        {/if}
    </div>
</div>
