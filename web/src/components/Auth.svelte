<script lang="ts">
    import { onMount } from "svelte";
    import { checkAuth } from "../utils/backend";
    import { JWT, username } from "../utils/auth";
    
    export let checked = false;
    export let auth = false;

    onMount(async () => {
        auth = await checkAuth();
        checked = true;
    });

    username.subscribe(async () => {
        auth = await checkAuth();
    })
</script>
