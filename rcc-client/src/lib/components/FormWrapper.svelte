<script lang="ts">
    export let modalTitle: string;
    export let modalBody: string;
</script>

<div class="card p-4 w-modal shadow-xl space-y-4" data-testid="modal-card">
    <header class="text-2xl font-bold" data-testid="modal-title">{modalTitle}</header>
    <article data-testid="modal-body">{modalBody}</article>
    <slot/>
</div>