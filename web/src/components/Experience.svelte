<script>
    import DotsIcon from '$lib/images/icon/black_dots.svg?raw'
    import MtsImage from '$lib/images/job/mts.png'
    import SberImage from '$lib/images/job/sber.png'
    import CristalixImage from '$lib/images/job/cristalix.png'
    import Job from './Job.svelte';
</script>

<div class="flex flex-col gap-[100rem]">
    <div class="flex flex-col gap-[30rem] lg:gap-0 lg:flex-row max-w-[830rem] lg:max-w-[1720rem]">
        <p class="min-w-[900rem] text-[96rem] font-bold font-oddval">
            ОПЫТ
        </p>
        <div class="flex w-full">
            <p class="text-[32rem] font-onest w-full">
                Должности
            </p>
            {@html DotsIcon}
        </div>
    </div>
    <div class="max-w-[1720rem] lg:max-w-[1720rem] flex flex-wrap gap-x-[80rem] gap-y-[60rem]">
        <Job 
            jobName="МТС БАНК" 
            icon={MtsImage} 
            position="Старший разработчик Java" 
            date="Москва, c 2023 г."
            actual={true}
        />
        <Job 
            jobName="СБЕР" 
            icon={SberImage} 
            position="Ведущий ИТ-инженер" 
            date="Москва, 2022-2023 г."
        />
        <Job 
            jobName="CRISTALIX" 
            icon={CristalixImage} 
            position="Разработчик игровой платформы" 
            date="Москва, 2019-2022 г."
        />
    </div>
</div>