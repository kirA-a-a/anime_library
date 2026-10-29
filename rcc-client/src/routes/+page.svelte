<script>
	import MainPageMenuItem from "$lib/components/MainPageMenuItem.svelte";
</script>


<nav aria-label="Основная навигация" data-testid="main-page">
	<h1 class="text-3xl text-center m-14" data-testid="main-page-title">Ваши любимые картины и художники всегда рядом</h1>

	<ul class="grid grid-cols-1 md:grid-cols-3 gap-8 p-4 w-4/5 mx-auto" data-testid="main-menu">
		<MainPageMenuItem
				link="/painting"
				linkText="Картины"
				src="/piones-dtuchs.jpeg"
				altText="Перейти к галерее картин"
				dataTestId="main-menu-painting"
		/>
		<MainPageMenuItem
				link="/artist"
				linkText="Художники"
				src="/renuar.jpeg"
				altText="Перейти к списку художников"
				dataTestId="main-menu-artist"
		/>
		<MainPageMenuItem
				link="/museum"
				linkText="Музеи"
				src="/hermitage.jpg"
				altText="Перейти к списку музеев"
				dataTestId="main-menu-museum"
		/>
	</ul>
</nav>



