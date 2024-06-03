<script>
	import "../app.css";
	import Introduction from "../components/Introduction.svelte";
	import Experience from "../components/Experience.svelte";
	import Portfolio from "../components/Portfolio.svelte";
	import Footer from "../components/Footer.svelte";
</script>

<div class="w-full flex justify-center p-[100rem]">
	<div class="w-full max-w-[1720rem] text-[32rem] flex flex-col gap-[200rem] ra">
		<Introduction />
		<Experience />
		<Portfolio />
		<Footer />
	</div>
</div>
