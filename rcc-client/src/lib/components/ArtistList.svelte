<script lang="ts">
	import { Avatar } from "@skeletonlabs/skeleton";
    import { artistsStore } from '$lib/stores/artist.store';

    let loadedImages = new Set<string>();
    
    function handleImageLoad(id: string) {
        loadedImages.add(id);
        loadedImages = loadedImages;
    }
</script>

<ul class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-4 p-8" data-testid="artist-list">
    {#each $artistsStore.data as artist(artist.id)}
        <li data-testid="artist-card-item" data-artist-id={artist.id}>
            <a href={`/artist/${artist.id}`} class="flex flex-col justify-center items-center" data-testid="artist-card" data-artist-id={artist.id}>
                {#if !loadedImages.has(artist.id)}
                    <div class="skeleton w-48 h-48 rounded-full" data-testid="artist-card-skeleton"></div>
                {:else}
                    <div data-testid="artist-card-avatar">
                        <Avatar src={artist.photo} width="w-48" rounded="rounded-full" />
                    </div>
                {/if}
                <img 
                    class="hidden"
                    on:load={() => handleImageLoad(artist.id)}
                    src={artist.photo} 
                    alt={artist.name}
                    data-testid="artist-card-image-preload"
                >
                <span class="flex-auto py-4" data-testid="artist-card-name">{artist.name}</span>
            </a>
        </li>
    {/each}
</ul>
