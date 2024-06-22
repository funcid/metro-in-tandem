<script lang="ts">
	import { onMount } from 'svelte';
	import Router, { push } from "svelte-spa-router";
	import { fetchMetroStations } from "../utils/metro"

	import Applications from '../components/applications/Applications.svelte';
	import ApplicationDetail from '../components/applications/ApplicationDetail.svelte';
    import CreateApplication from "../components/applications/CreateApplication.svelte";

	import Passengers from '../components/passengers/Passengers.svelte';
	import PassengerDetail from '../components/passengers/PassengerDetail.svelte';
	import CreatePassenger from '../components/passengers/CreatePassenger.svelte';

	import Employees from '../components/employees/Employees.svelte';
	import EmployeeDetail from '../components/employees/EmployeeDetail.svelte';
	import CreateEmployee from '../components/employees/CreateEmployee.svelte';

	import Distribution from '../components/distribution/Distribution.svelte';

	import Login from '../components/login/Login.svelte';
	import Home from '../components/home/Home.svelte';
	import Header from '../components/Header.svelte';
	import Footer from '../components/Footer.svelte';
    import Auth from '../components/Auth.svelte';
  
	const routes = {
	  '/': Home,
	  '/applications': Applications,
	  '/applications/:id': ApplicationDetail,
	  '/create-application/:id': CreateApplication,
	  '/passengers': Passengers,
	  '/passengers/:id': PassengerDetail,
	  '/create-passenger': CreatePassenger,
	  '/distribution': Distribution,
	  '/employees': Employees,
	  '/employees/:id': EmployeeDetail,
	  '/create-employee': CreateEmployee,
	  '/login': Login,
	};

	let location = "";
  
	const handleRoute = async (event: any) => {
		// console.log("AAAA");
		// let checkAuthResult = await checkAuth();
		// console.log(checkAuthResult);

		location = event.detail.route;
	  	// if (!checkAuthResult && event.detail.route !== '/' && event.detail.route !== '/login') {
		// 	push('/login');
	  	// }
	};

	let authChecked = false;
	let authOk = false;

	$: if (authChecked && !authOk && location !== "/login") {
		push("/login")
	} else if (authChecked && authOk && location === "/login") {
		push("/")
	}


    onMount(async () => {
		await fetchMetroStations();
	})
  </script>
  
  <svelte:head>
	<title>Метро в тандеме</title>
	<meta name="description" content="Метро в тандеме" />
  </svelte:head>
  
  <Auth bind:checked={authChecked} bind:auth={authOk}></Auth>

  <div class="w-full min-h-[calc(100vh-200rem)] flex justify-center p-[100rem] lg:py-[60rem] lg:px-[120rem] font-moscowsans">
	<div class="w-full justify-between text-[32rem] flex flex-col gap-[100rem]">
	  <Header />
	  <Router {routes} on:routeLoaded={handleRoute} />
	  <Footer />
	</div>
  </div>
  
  <style global>
	@font-face {
	  font-family: "MoscowSans";
	  src: url("/public/fonts/moscowsans-regular.ttf") format("truetype");
	  font-style: normal;
	}
  </style>