<script lang="ts">
	export let submitButtonText = 'Добавить';
	export let closeButtonText = 'Закрыть';
	export let onClose = () => {};
	export let isLoading = false;
</script>

<div class="text-right" data-testid="modal-button-group">
	<button class="btn variant-ringed" type="button" data-testid="modal-cancel-button" on:click={onClose} disabled={isLoading}
		>{closeButtonText}</button
	>
	<button class="btn variant-filled-primary" type="submit" data-testid="modal-submit-button" disabled={isLoading}>
		{#if isLoading}
			<svg
				class="animate-spin h-5 w-5 mr-2 inline-block"
				xmlns="http://www.w3.org/2000/svg"
				fill="none"
				viewBox="0 0 24 24"
				data-testid="modal-submit-spinner"
			>
				<circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" data-testid="modal-submit-spinner-track" />
				<path
					class="opacity-75"
					fill="currentColor"
					d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
					data-testid="modal-submit-spinner-path"
				/>
			</svg>
		{/if}
		{submitButtonText}
	</button>
</div>
