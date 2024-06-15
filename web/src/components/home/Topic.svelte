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
    class="lg:min-w-[820rem] min-w-[830rem] lg:h-[200rem] flex gap-[30rem] cursor-pointer hover:bg-gray-100 rounded-[20rem] p-[30rem]"
>
    <img class="w-[220rem] h-[220rem] lg:w-[auto] lg:h-[auto]" src={icon} alt={jobName} />
    <div class="w-full flex justify-between">
        <div class="flex flex-col h-full gap-[16rem] lg:gap-0 lg:justify-between">
            <p class="text-[56rem] font-bold font-oddval">{jobName}</p>
            <p class="font-onest text-[28rem]/[128%] lg:text-[24rem]">
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
