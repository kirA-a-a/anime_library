<script lang="ts">
    export let isBigScreen: boolean;
    const suffix = isBigScreen ? "" : "-mobile";
</script>
<nav class={`list-nav ${isBigScreen ? "hidden md:block" : "md:hidden w-full"}`} data-testid={`nav${suffix}`}>
    <ul class={`flex items-baseline ${!isBigScreen ? "justify-around" : ""}`} data-testid={`nav-list${suffix}`}>
        <li data-testid={`nav-item-painting${suffix}`}>
            <a href="/painting" data-testid={`nav-painting${suffix}`}>
                Картины
            </a>
        </li>
        <li data-testid={`nav-item-artist${suffix}`}>
            <a href="/artist" data-testid={`nav-artist${suffix}`}>
                Художники
            </a>
        </li>
        <li data-testid={`nav-item-museum${suffix}`}>
            <a href="/museum" data-testid={`nav-museum${suffix}`}>
                Музеи
            </a>
        </li>
    </ul>
</nav>
