<script lang="ts">
	import viewport from "$lib/hooks/useViewport";
	import type { Pageable } from "$lib/types/Pageable";
	import { onMount } from "svelte";

    export let label: string;
    export let value: string | undefined;
    export let error: string;
    export let name: string;
    export let required = false;
    export let keyName = "id";
    export let valueName  = "name";
    export let loadFunction: ({page}: {page?: number}) => Promise<{data?: Pageable<[]>, error?: string}>;

    let currentPage = 0;
    let data: any [] = [];
    let noMoreData: boolean;

    onMount(async() => {
        const res = await loadFunction({page: currentPage});
        if(res.error) {
            console.warn(res.error);
            return;
        }
        const resData = res.data;
        if(resData){
            data = resData.content;
            noMoreData =  currentPage === resData.totalPages - 1;
        }
    });

    const loadMore = async () => {
        const response = await loadFunction({page: ++currentPage});
        if(response.error) {
            console.warn(response.error);
            return;
        }
        const resData= response.data;
        if(resData){
            const newBatch = resData.content;
            data = [...data, ...newBatch];
            noMoreData = currentPage === resData.totalPages - 1;
        }
    }

</script>


<label class="label" data-testid={`field-${name}`}>
    <span data-testid={`label-${name}`}>{label}</span>
    <select 
        class="select" 
        size={3}
        bind:value={value}
        on:select={() => error = ""}
        {required}
        {name}
        data-testid={`input-${name}`}
        >
    {#each data as item(item[keyName])}
        {#if item[keyName] === data[data.length - 1][keyName]}
        <option
            use:viewport
            value={item[keyName]}
            data-testid={`option-${name}`}
            data-option-id={item[keyName]}
            on:viewportenter|once={() => {
                if(!noMoreData){
                    loadMore();
                }
            }}>
            {item[valueName]}
        </option>
        {:else}
            <option value={item[keyName]} data-testid={`option-${name}`} data-option-id={item[keyName]}>{item[valueName]}</option>
        {/if}
    {/each}
    </select>
    <span class="text-error-400" data-testid={`input-error-${name}`}>{error}</span>
</label>

