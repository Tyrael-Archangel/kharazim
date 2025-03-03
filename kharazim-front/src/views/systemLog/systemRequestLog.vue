<template>
  <el-tabs v-model="tabModel" type="card">
    <el-tab-pane label="系统请求日志" name="systemLogTab">
      <RequestLogTable ref="requestLogTableRef" />
    </el-tab-pane>
    <el-tab-pane label="endpoints" name="endpointTab">
      <EndpointManage @choice-endpoint="showEndpointLogs" />
    </el-tab-pane>
    <el-tab-pane label="配置状态" name="configStatusTab">
      <RequestLogConfig />
    </el-tab-pane>
  </el-tabs>
</template>

<script lang="ts" setup>
import RequestLogTable from "./requestLogTable.vue";
import EndpointManage from "./endpointManage.vue";
import RequestLogConfig from "./requestLogConfig.vue";
import { ref } from "vue";

const tabModel = ref("systemLogTab");

const requestLogTableRef = ref<InstanceType<typeof RequestLogTable>>();

function showEndpointLogs(endpoint: string) {
  tabModel.value = "systemLogTab";
  requestLogTableRef.value?.showEndpointLogs(endpoint);
}
</script>

<style scoped></style>
