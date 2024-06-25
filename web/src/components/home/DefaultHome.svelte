<script>
    // Author: Freepik - https://www.flaticon.com/packs/podcast-43
    import ApplicationImage from "$lib/images/home/advice.png";
    import PassengerImage from "$lib/images/home/question.png";
    import EmployeeImage from "$lib/images/home/team.png";
    import DistributionImage from "$lib/images/home/distribution.png";
    import Topic from "./Topic.svelte";
</script>

<div
    class="max-w-[1720rem] h-full justify-between flex flex-wrap gap-x-[80rem] gap-y-[60rem]"
>
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
    <Topic
        jobName="Распределение"
        icon={DistributionImage}
        date="Распределяет все заявки на"
        position="сопровождение по работникам"
        route="distribution"
    />
</div>
