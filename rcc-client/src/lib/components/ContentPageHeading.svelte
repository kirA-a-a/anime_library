<script lang="ts">
	import { SearchIcon } from "$lib/types/Icon";
    import {sessionStore} from "$lib/stores/sessionStore";

    
    let search = "";

    export let searchPlaceholder: string;
    export let title: string;
    export let addButtonName: string;
    export let addButtonTestId: string;
    export let onAddButtonClick: () => void;
    export let loadFunction: (search: string) => {};

</script>

<div class="flex items-center justify-between m-4" data-testid="page-heading">
    <h2 class="text-3xl m-4" data-testid="page-title">{title}</h2>
    {#if $sessionStore.user}
        <button type="button" class="btn variant-filled-primary ml-4" data-testid={addButtonTestId} on:click={onAddButtonClick}>{addButtonName}</button>
    {/if}
</div>

<div class="flex justify-center mb-4 mx-8" data-testid="search-bar">
    <input class="input" data-testid="search-input" bind:value={search} title={searchPlaceholder} type="search" placeholder={searchPlaceholder} on:keypress={(evt) => {
        if(evt.key === "Enter") {
            loadFunction(search);
        }
    }}/>
    <button type="button" class="btn-icon variant-soft-surface ml-4" data-testid="search-button" on:click={() => loadFunction(search)}>
        <img src={SearchIcon} alt="Иконка поиска" data-testid="search-icon"/>
    </button>
</div>