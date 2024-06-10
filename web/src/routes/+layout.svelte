<script>
	import "../app.css";
	import Header from "../components/Header.svelte";
	import Experience from "../components/Experience.svelte";
	import Portfolio from "../components/Portfolio.svelte";
	import Footer from "../components/Footer.svelte";
</script>

<div class="w-full flex justify-center py-[60rem] px-[120rem] font-moscowsans">
	<div class="w-full text-[32rem] flex flex-col gap-[200rem] ra">
		<Header />
		<Experience />
		<Portfolio />
		<Footer />
	</div>
</div>
