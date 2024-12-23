<template>
  <el-tabs type="card">
    <el-tab-pane label="系统请求日志">
      <RequestLogTable />
    </el-tab-pane>
    <el-tab-pane label="endpoints">
      <EndpointManage />
    </el-tab-pane>
  </el-tabs>
</template>

<script lang="ts" setup>
import RequestLogTable from "./requestLogTable.vue";
import EndpointManage from "./endpointManage.vue";
</script>

<style scoped></style>
