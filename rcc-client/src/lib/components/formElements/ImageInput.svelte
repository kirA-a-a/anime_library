<script lang="ts">

    export let label: string;
    export let name: string;
    export let files: FileList;
    export let error: string;
    export let placeholder = label;
    export let required = false;
    const authorizedExtensions = ['.jpg', '.jpeg', '.png', '.webp'];

</script>

<label class="label" data-testid={`field-${name}`}>
    <span data-testid={`label-${name}`}>{label}</span>
    <input 
        {name}
        data-testid={`input-${name}`}
        class="input"
        class:input-error={error.length}
        type="file" 
        bind:files={files} 
        on:input={() => error = ""}
        accept={authorizedExtensions.join(',')}
        {placeholder}
        {required}
        />
    <span class="text-error-400" data-testid={`input-error-${name}`}>{error}</span>
</label>