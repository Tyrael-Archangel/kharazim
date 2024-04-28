<template></template>
<script lang="ts" setup>
import { useRouter } from "vue-router";
import { onMounted } from "vue";

const router = useRouter();

onMounted(() => {
  router.push("/login");
});
</script>

<style scoped></style>