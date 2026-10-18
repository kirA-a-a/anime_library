<script lang="ts">

    import {getToastStore, type ToastSettings} from "@skeletonlabs/skeleton";

    const toastStore = getToastStore();

    const triggerErrorToaster = (message: string) => {
        const t: ToastSettings = {
            message: message,
            background: 'variant-filled-error',
            // Skeleton Toast already has data-testid="toast"; class marks type for e2e
            classes: "break-all toast-error",
        };
        toastStore.trigger(t);
    }

    const triggerSuccessToaster = (message: string) => {
        const t: ToastSettings = {
            message: message,
            background: 'variant-filled-primary',
            classes: "break-all toast-success",
        };
        toastStore.trigger(t);
    }
</script>

<slot triggerError={triggerErrorToaster} triggerSuccess={triggerSuccessToaster}/>
