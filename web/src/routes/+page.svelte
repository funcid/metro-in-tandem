<script lang="ts">
	import Router, { push } from "svelte-spa-router";
	import { isAuthenticated } from "../components/login/Login.svelte"

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
  
	const routes = {
	  '/': Home,
	  '/applications': Applications,
	  '/applications/:id': ApplicationDetail,
	  '/passengers': Passengers,
	  '/passengers/:id': PassengerDetail,
	  '/create-passenger': CreatePassenger,
	  '/distribution': Distribution,
	  '/employees': Employees,
	  '/employees/:id': EmployeeDetail,
	  '/create-employee': CreateEmployee,
	  '/login': Login,
	  '/create-application/:id': CreateApplication,
	};
  
	const handleRoute = (event: any) => {
	  	if (!$isAuthenticated && event.detail.route !== '/' && event.detail.route !== '/login') {
			push('/login');
	  	}
	};
  </script>
  
  <svelte:head>
	<title>Home</title>
	<meta name="description" content="Funcfolio" />
  </svelte:head>
  
  <div class="w-full flex justify-center py-[60rem] px-[120rem] font-moscowsans">
	<div class="w-full text-[32rem] flex flex-col gap-[120rem]">
	  <Header />
	  <Router {routes} on:routeLoaded={handleRoute} />
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