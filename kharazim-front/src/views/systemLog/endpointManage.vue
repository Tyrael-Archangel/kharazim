<template>
  <el-table :data="endpoints">
    <el-table-column label="endpoint" prop="endpoint" />
    <el-table-column align="center" label="是否活跃" prop="active">
      <template v-slot="{ row }">
        <el-tag v-if="row.active" type="success">是</el-tag>
        <el-tag v-else type="info">否</el-tag>
      </template>
    </el-table-column>
    <el-table-column align="center" label="是否允许记录系统日志" prop="enableSystemLog">
      <template v-slot="{ row }">
        <el-switch
          v-model="row.enableSystemLog"
          :active-value="true"
          :inactive-value="false"
          @change="switchEnableLog(row)"
        />
      </template>
    </el-table-column>
  </el-table>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";

interface Endpoint {
  endpoint: string;
  active: boolean;
  enableSystemLog: boolean;
}

const endpoints = ref<Endpoint[]>([]);

function loadEndpoints() {
  axios
    .get("/kharazim-api/system/request-log/endpoints")
    .then((response: AxiosResponse) => (endpoints.value = response.data.data));
}

function switchEnableLog(endpoint: Endpoint) {
  const url = endpoint.enableSystemLog
    ? "/kharazim-api/system/request-log/enable/endpoint"
    : "/kharazim-api/system/request-log/disable/endpoint";
  axios.put(url, null, {
    params: {
      endpoint: endpoint.endpoint,
    },
  });
}

onMounted(() => {
  loadEndpoints();
});
</script>

<style scoped />
