<script>
    import ApplicationImage from '$lib/images/home/advice.png'
    import PassengerImage from '$lib/images/home/question.png'
    import EmployeeImage from '$lib/images/home/team.png'
    import Topic from './Topic.svelte';
</script>

<div class="flex flex-col gap-[100rem]">
    <div class="max-w-[1720rem] lg:max-w-[1720rem] flex flex-wrap gap-x-[80rem] gap-y-[60rem]">
        <Topic 
            jobName="Заявки" 
            icon={ApplicationImage} 
            date="Просмотр, редактирование и создание"
            position="заявок на сопровождение" 
            route="applications" 
            actual={true}
        />
        <Topic 
            jobName="Пассажиры" 
            icon={PassengerImage} 
            date="Найти конкретного пассажира или"
            position="создать нового" 
            route="passengers" 
        />
        <Topic 
            jobName="Команда" 
            icon={EmployeeImage} 
            date="Отображение сотрудников по участку,"
            position="настройка прав"
            route="employees" 
        />
    </div>
</div>