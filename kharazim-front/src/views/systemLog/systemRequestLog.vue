<template>
  <el-tabs v-model="tabModel" type="card">
    <el-tab-pane label="系统请求日志" name="systemLogTab">
      <RequestLogTable ref="requestLogTableRef" />
    </el-tab-pane>
    <el-tab-pane label="endpoints" name="endpointTab">
      <EndpointManage @choice-endpoint="showEndpointLogs" />
    </el-tab-pane>
    <el-tab-pane label="配置状态" name="configStatusTab">
      <el-card shadow="never">
        <el-space>
          <el-text size="large">系统记录请求日志状态:</el-text>
          <el-icon :color="systemEnableRequestLog ? 'green' : 'orange'" size="large" style="margin-left: 50px">
            <SuccessFilled v-if="systemEnableRequestLog" />
            <CircleCloseFilled v-else />
          </el-icon>
          <el-text>{{ systemEnableRequestLog ? "已开启" : "已关闭" }}</el-text>
        </el-space>
      </el-card>
      <br />
      <el-card v-if="systemEnableRequestLog" shadow="never">
        <template #header>
          <el-text size="large">忽略记录请求日志的URL:</el-text>
        </template>
        <li v-for="ignoredUrl in configIgnoredUrls" style="padding-left: 10px">
          {{ ignoredUrl }}
        </li>
      </el-card>
    </el-tab-pane>
  </el-tabs>
</template>

<script lang="ts" setup>
import RequestLogTable from "./requestLogTable.vue";
import EndpointManage from "./endpointManage.vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";
import { onMounted, ref } from "vue";
import { CircleCloseFilled, SuccessFilled } from "@element-plus/icons-vue";

const systemEnableRequestLog = ref(false);
const configIgnoredUrls = ref<string[]>([]);

function loadIgnoreRequestLogUrls() {
  axios.get("/kharazim-api/system/request-log/config-ignored-urls").then((response: AxiosResponse) => {
    const responseCode = response.data.code;
    if (responseCode != 200) {
      systemEnableRequestLog.value = false;
    } else {
      systemEnableRequestLog.value = true;
      configIgnoredUrls.value = response.data.data;
    }
  });
}

const tabModel = ref("systemLogTab");

const requestLogTableRef = ref<InstanceType<typeof RequestLogTable>>();

function showEndpointLogs(endpoint: string) {
  tabModel.value = "systemLogTab";
  requestLogTableRef.value?.showEndpointLogs(endpoint);
}

onMounted(() => {
  loadIgnoreRequestLogUrls();
});
</script>

<style scoped></style>
