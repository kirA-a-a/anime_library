<script lang="ts">
    import type {PaintingType} from "$lib/types/Painting";

    export let paintings: PaintingType[];
    
    let loadedImages = new Set<string>();
    
    function handleImageLoad(id: string) {
        loadedImages.add(id);
        loadedImages = loadedImages;
    }
</script>


<ul class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 p-4" data-testid="painting-list">
    {#each paintings as painting(painting.id)}
        <li class="relative" data-testid="painting-card-item" data-painting-id={painting.id}>
            <a href={`/painting/${painting?.id}`} data-testid="painting-card" data-painting-id={painting.id}>
                {#if !loadedImages.has(painting.id)}
                    <div class="skeleton h-96 w-full rounded-lg" data-testid="painting-card-skeleton"></div>
                {/if}
                <img 
                    class="max-w-full rounded-lg object-cover w-full h-96 transition-opacity duration-300"
                    class:opacity-0={!loadedImages.has(painting.id)}
                    class:absolute={!loadedImages.has(painting.id)}
                    on:load={() => handleImageLoad(painting.id)}
                    src={painting.content} 
                    alt={painting.title}
                    loading="lazy"
                    decoding="async"
                    data-testid="painting-card-image"
                >
                <div class="text-center mt-2" data-testid="painting-card-title">{painting?.title}</div>
            </a>
        </li>
    {/each}
</ul>
