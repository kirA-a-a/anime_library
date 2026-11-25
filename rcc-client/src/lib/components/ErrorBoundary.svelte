<script lang="ts">
    import { onMount } from 'svelte';
    
    export let fallback: string = 'Произошла ошибка при загрузке компонента';
    let hasError = false;
    let errorMessage = '';
    
    onMount(() => {
        const handleError = (event: ErrorEvent) => {
            hasError = true;
            errorMessage = event.message || fallback;
            console.error('Error caught by ErrorBoundary:', event.error);
        };
        
        window.addEventListener('error', handleError);
        
        return () => {
            window.removeEventListener('error', handleError);
        };
    });
    
    function handleReload() {
        hasError = false;
        window.location.reload();
    }
</script>

{#if hasError}
    <div class="alert variant-filled-error m-4 p-6" data-testid="error-boundary">
        <div class="alert-message" data-testid="error-boundary-message-box">
            <h3 class="h3 mb-2" data-testid="error-boundary-title">Ошибка</h3>
            <p class="mb-4" data-testid="error-boundary-message">{errorMessage}</p>
            <button 
                class="btn variant-filled"
                data-testid="error-boundary-reload"
                on:click={handleReload}
            >
                Перезагрузить страницу
            </button>
        </div>
    </div>
{:else}
    <slot />
{/if}
