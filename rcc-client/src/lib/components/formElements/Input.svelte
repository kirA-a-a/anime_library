<script lang="ts">

    export let label: string;
    export let name: string;
    export let value: string;
    export let error: string;
    export let placeholder = label;
    export let required = false;


</script>

<label class="label" data-testid={`field-${name}`}>
    <span data-testid={`label-${name}`}>{label}</span>
    <input 
        {name}
        data-testid={`input-${name}`}
        class="input"
        autocomplete="off"
        class:input-error={error.length}
        type="text" 
        bind:value={value} 
        on:input={() => error = ""}
        {placeholder}
        {required}
        />
    <span class="text-error-400" data-testid={`input-error-${name}`}>{error}</span>
</label>