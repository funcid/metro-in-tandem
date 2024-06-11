<script>
	import Router from "svelte-spa-router";
  
	import Header from '../components/Header.svelte';
	import Home from '../components/Experience.svelte';
	import Applications from '../components/applications/Applications.svelte';
	import ApplicationDetail from '../components/applications/ApplicationDetail.svelte';
	import Passengers from '../components/passangers/Passangers.svelte';
	import Employees from '../components/Employee.svelte';
	import Distribution from '../components/SocialMedia.svelte';
	import Team from '../components/Footer.svelte';
	import Login from '../components/login/Login.svelte';
	import Footer from '../components/Footer.svelte';

	const routes = {
		'/': Home,
	  	'/applications': Applications,
	  	'/applications/:id': ApplicationDetail,
	  	'/passengers': Passengers,
	  	'/employees': Employees,
	  	'/distribution': Distribution,
	  	'/team': Team,
	  	'/login': Login
	};
</script>

<svelte:head>
	<title>Home</title>
	<meta name="description" content="Funcfolio" />
</svelte:head>

<div class="w-full flex justify-center py-[60rem] px-[120rem] font-moscowsans">
	<div class="w-full text-[32rem] flex flex-col gap-[120rem]">
		<Header />
		<Router {routes} />
		<Footer />
	</div>
</div>

<style>
	@font-face {
		font-family: "MoscowSans";
		src: url("/public/fonts/moscowsans-regular.ttf") format("truetype");
		font-style: normal;
	}
</style>
