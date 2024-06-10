<script>
    import Login from '$lib/images/login.svg'
    import Navigation from '$lib/images/navigation.svg?raw'
</script>

<div class="flex flex-col gap-[40rem]">
    <div class="flex justify-between items-center">
        <img src="https://apps3proxy.mosmetro.tech/webapp-mosmetro/mm-logo-red.svg" alt="#" width="221rem" height="56.61rem" />
        <div class="flex gap-[60rem] items-center">
            <button class="flex gap-[20rem] bg-[#D4212D] py-[12rem] px-[26rem] rounded-[12rem] items-center">
                <a href="#/login" class="text-white text-[27rem]">Войти</a> 
                <img src={Login} alt="#" width="27rem" height="27rem"/>
            </button>
        </div>
    </div>
    <div class="flex justify-between">
        <a href="#/" class="topic">Главная</a>
        <a href="#/applications" class="topic">Заявки</a>
        <a href="#/passengers" class="topic">Пассажиры</a>
        <a href="#/employees" class="topic">Сотрудники</a>
        <a href="#/distribution" class="topic">Распределение</a>
        <a href="#/team" class="topic">Команда</a>
    </div>
</div>