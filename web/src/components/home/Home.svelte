<script>
    import { role } from "../../utils/auth";

	import DefaultHome from './DefaultHome.svelte';
    import EmployeeHome from './EmployeeHome.svelte';
</script>

<div class="flex flex-col gap-[100rem] h-full">
    {#if $role != 'Сотрудник'}
        <DefaultHome/>
    {:else}
        <EmployeeHome/>
    {/if}
</div>
