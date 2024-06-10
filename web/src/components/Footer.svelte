<script>
    import { page } from '$app/stores';  
</script>

<div class="flex justify-between font-onest">
    <div class="text-left">
        <a>{$page.url.host}, {new Date().getFullYear()}</a><br/>
        Сделано для хакатона ЛЦТ<br/>
    </div>
    <div class="text-right">
        <a href="https://funcfolio.ru">Разработчик сервиса</a><br/>
        <a href="https://t.me/funcid">Контакты</a><br/>
    </div>
</div>