<template>
  <div class="bread-wrap">
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item v-for="item in breadList">
        <span>{{ item }}</span>
      </el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script lang="ts" setup>
import { ArrowRight } from "@element-plus/icons-vue";
import { useRoute } from "vue-router";
import { computed } from "vue";

const route = useRoute();

const breadList = computed(() => {
  return route.meta.fullPath;
});
</script>

<style scoped>
.bread-wrap {
  padding: 10px 0;
}
</style>