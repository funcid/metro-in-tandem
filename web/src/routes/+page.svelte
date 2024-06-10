<script>
	import Router from "svelte-spa-router";

	import Header from '../components/Header.svelte';
	import Home from '../components/Experience.svelte';
	import Requests from '../components/Portfolio.svelte';
	import Passengers from '../components/Job.svelte';
	import Employees from '../components/Project.svelte';
	import Distribution from '../components/SocialMedia.svelte';
	import Team from '../components/Footer.svelte';

    const routes = {
        '/': Home,
        '/requests': Requests,
        '/passengers': Passengers,
        '/employees': Employees,
        '/distribution': Distribution,
        '/team': Team
    };
</script>

<svelte:head>
	<title>Home</title>
	<meta name="description" content="Funcfolio" />
</svelte:head>

<div class="w-full flex justify-center py-[60rem] px-[120rem] font-moscowsans">
	<div class="w-full text-[32rem] flex flex-col gap-[200rem] ra">
		<Header />
		<Router {routes} />
	</div>
</div>

<style>
	@font-face {
		font-family: "MoscowSans";
		src: url("/public/fonts/moscowsans-regular.ttf") format("truetype");
		font-style: normal;
	}
</style>
