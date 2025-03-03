<template>
  <el-card shadow="never">
    <el-space>
      <el-text size="large">系统记录请求日志状态:</el-text>
      <el-icon :color="systemEnableRequestLog ? 'green' : 'orange'" size="large" style="margin-left: 50px">
        <WarningFilled v-if="systemEnableRequestLog === null" />
        <SuccessFilled v-if="systemEnableRequestLog !== null && systemEnableRequestLog" />
        <CircleCloseFilled v-if="systemEnableRequestLog !== null && !systemEnableRequestLog" />
      </el-icon>
      <el-text>{{ systemEnableRequestLog === null ? "未知" : systemEnableRequestLog ? "已开启" : "已关闭" }}</el-text>
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
</template>

<script lang="ts" setup>
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";
import { onMounted, ref } from "vue";
import { CircleCloseFilled, SuccessFilled, WarningFilled } from "@element-plus/icons-vue";

const systemEnableRequestLog = ref<boolean | null>(null);
const configIgnoredUrls = ref<string[]>([]);

function loadIgnoreRequestLogUrls() {
  axios
    .get("/kharazim-api/system/request-log/config-ignored-urls", {
      disablePrintGlobalError: true,
    })
    .then((response: AxiosResponse) => {
      const responseCode = response.data.code;
      if (responseCode != 200) {
        systemEnableRequestLog.value = false;
      } else {
        systemEnableRequestLog.value = true;
        configIgnoredUrls.value = response.data.data;
      }
    });
}

onMounted(() => {
  loadIgnoreRequestLogUrls();
});
</script>

<style scoped />
