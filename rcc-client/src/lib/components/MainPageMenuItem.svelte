<script lang="ts">
    export let link: string;
    export let src: string;
    export let altText: string;
    export let linkText: string;
    export let dataTestId: string;
</script>

<li data-testid={`${dataTestId}-item`}>
    <a href={link} data-testid={dataTestId}>
        <img class="rounded-lg object-cover w-72 h-96 mx-auto" {src} alt={altText} data-testid={`${dataTestId}-image`}>
        <div class="text-center text-2xl p-6 text-bold" data-testid={`${dataTestId}-text`}>{linkText}</div>
    </a>
</li>