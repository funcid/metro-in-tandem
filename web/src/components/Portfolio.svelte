<script>
    import DotsIcon from "$lib/images/icon/black_dots.svg?raw";
    import Project from "./Project.svelte";
    import PROJECTS from "../constants/projects"
</script>

<div class="flex flex-col gap-[80rem]">
    <div class="flex lg:flex-row flex-col gap-[30rem] lg:gap-0 max-w-[830rem] lg:max-w-[1720rem]">
        <p class="min-w-[900rem] text-[96rem] font-bold font-oddval">
            ПОРТФОЛИО
        </p>
        <div class="flex w-full justify-between">
            <p class="text-[32rem] font-onest w-full">Пет проекты и опенсорс</p>
            {@html DotsIcon}
        </div>
    </div>

    <div class="max-w-[1720rem] flex lg:grid grid-cols-1 lg:grid-cols-2 flex-wrap gap-[80rem]">
        {#each [...PROJECTS] as elem}
            <Project
                background={elem.background}
                title={elem.title}
                description={elem.description}
                color={elem.color}
                blockSize={elem.blockSize}
                height={elem.height}
                additionalStyle={elem.additionalStyle}
                url={elem.url}
            />
        {/each}
    </div>
</div>
