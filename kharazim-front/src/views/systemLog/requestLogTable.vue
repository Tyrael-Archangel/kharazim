<template>
  <div>
    <el-form :inline="true" :model="pageRequest" class="page-form-block" @keyup.enter="loadSystemRequestLogs">
      <el-form-item label="请求用户">
        <el-select
          v-model="pageRequest.userCode"
          :remote-method="async (query: any) => (users = await loadSimpleUsers(query))"
          clearable
          filterable
          placeholder="选择请求用户"
          remote
          width="500px"
        >
          <el-option v-for="item in users" :key="item.code" :label="item.nickName" :value="item.code">
            <el-image v-if="item.avatarUrl" :src="item.avatarUrl" style="float: left; width: 30px" />
            <span style="float: right">{{ item.nickName }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="响应状态码">
        <el-input v-model="pageRequest.responseStatus" clearable placeholder="响应状态码" />
      </el-form-item>
      <el-form-item label="endpoint">
        <el-input v-model="pageRequest.endpoint" clearable placeholder="endpoint" />
      </el-form-item>
      <el-form-item label="请求开始时间">
        <el-date-picker
          v-model="pageRequest.startTimeRange"
          end-placeholder="截止时间"
          start-placeholder="开始时间"
          style="width: 350px"
          type="datetimerange"
        />
      </el-form-item>
      <el-form-item label="请求结束时间">
        <el-date-picker
          v-model="pageRequest.endTimeRange"
          end-placeholder="截止时间"
          start-placeholder="开始时间"
          style="width: 350px"
          type="datetimerange"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadSystemRequestLogs"> 查询</el-button>
        <el-button type="primary" @click="resetAndLoadSystemRequestLogs"> 重置</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div style="float: right; margin: 10px">
    <el-checkbox-group v-model="showColumns" size="small">
      <el-checkbox-button
        v-for="(column, key) in columnShow"
        :key="key"
        :label="key"
        :value="key"
        @change="handleColumnShowChange"
      >
        {{ column.name }}
      </el-checkbox-button>
    </el-checkbox-group>
  </div>
  <div>
    <el-table :data="requestLogPageData.data" border style="width: 100%; margin-top: 10px">
      <el-table-column align="center" label="ID" prop="id" width="90" />
      <el-table-column v-if="columnShow.endpoint.show" label="endpoint" prop="endpoint" width="260">
        <template v-slot="{ row }">
          <el-link type="primary" @click="loadSystemRequestLogsWithEndpoints(row.endpoint)">
            {{ row.endpoint }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column v-if="columnShow.uri.show" label="请求URI" prop="uri" width="250" />
      <el-table-column v-if="columnShow.remoteAddr.show" label="来源地址" prop="remoteAddr" width="140" />
      <el-table-column v-if="columnShow.forwardedFor.show" label="forwardedFor" prop="forwardedFor" width="120" />
      <el-table-column v-if="columnShow.realIp.show" label="realIp" prop="realIp" width="120" />
      <el-table-column v-if="columnShow.requestHeaders.show" label="请求头" width="600">
        <template v-slot="{ row }">
          <el-row v-for="requestHeader in row.requestHeaders">
            <el-col :span="6">{{ requestHeader.name + ":" }}</el-col>
            <el-col :span="18">{{ requestHeader.value }}</el-col>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column v-if="columnShow.responseHeaders.show" label="响应头" width="220">
        <template v-slot="{ row }">
          <el-row v-for="responseHeader in row.responseHeaders">
            <el-col :span="6">{{ responseHeader.name + ":" }}</el-col>
            <el-col :span="18">{{ responseHeader.value }}</el-col>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column v-if="columnShow.requestParams.show" label="请求参数" min-width="300">
        <template v-slot="{ row }">
          <el-row v-for="requestParam in row.requestParams">
            <el-col :span="12">{{ requestParam.name + ":" }}</el-col>
            <el-col :span="12">{{ requestParam.value }}</el-col>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column v-if="columnShow.requestBody.show" label="请求体" min-width="300">
        <template v-slot="{ row }">
          <el-text style="cursor: pointer" truncated @click="showDetailDialog(row.requestBody)">
            {{ row.requestBody }}
          </el-text>
        </template>
      </el-table-column>
      <el-table-column v-if="columnShow.responseBody.show" label="响应体" min-width="400">
        <template v-slot="{ row }">
          <el-text style="cursor: pointer" truncated @click="showDetailDialog(row.responseBody)">
            {{ row.responseBody }}
          </el-text>
        </template>
      </el-table-column>
      <el-table-column v-if="columnShow.userName.show" label="请求用户" prop="userName" width="100" />
      <el-table-column
        v-if="columnShow.responseStatus.show"
        align="center"
        label="状态码"
        prop="responseStatus"
        width="80"
      />
      <el-table-column v-if="columnShow.startTime.show" align="center" label="开始时间" prop="startTime" width="200" />
      <el-table-column v-if="columnShow.endTime.show" align="center" label="结束时间" prop="endTime" width="200" />
      <el-table-column
        v-if="columnShow.costMills.show"
        align="center"
        label="耗时(ms)"
        min-width="100"
        prop="costMills"
      />
    </el-table>
  </div>
  <div class="pagination-block">
    <el-pagination
      v-model:current-page="pageRequest.pageIndex"
      v-model:page-size="pageRequest.pageSize"
      :page-sizes="[10, 20, 50, 100]"
      :total="requestLogPageData.totalCount"
      background
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="loadSystemRequestLogs"
      @current-change="loadSystemRequestLogs"
    />
  </div>
  <el-dialog v-model="detailDialogVisible" title="详细信息" width="60%">
    <div style="display: flex; justify-content: flex-end; align-items: flex-end">
      <el-button @click="formatDialogContent">格式化</el-button>
      <el-button @click="copyToClipboard">复制</el-button>
    </div>
    <pre>{{ dialogContent }}</pre>
  </el-dialog>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, toRaw } from "vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";
import { dateTimeFormat } from "@/utils/DateUtil";
import { loadSimpleUsers, SimpleUser } from "@/views/user/user-list";
import { ElMessage } from "element-plus";

const requestLogPageData = ref({ totalCount: 0, data: [] });

const initPageRequest = {
  responseStatus: "",
  endpoint: "",
  userCode: "",
  startTimeRangeBegin: "",
  startTimeRangeEnd: "",
  startTimeRange: [] as Date[],
  endTimeRangeBegin: "",
  endTimeRangeEnd: "",
  endTimeRange: [] as Date[],
  pageIndex: 1,
  pageSize: 10,
};

const pageRequest = reactive({ ...initPageRequest });
const users = ref<SimpleUser[]>([]);

function resetAndLoadSystemRequestLogs() {
  Object.assign(pageRequest, initPageRequest);
  loadSystemRequestLogs();
}

function loadSystemRequestLogsWithEndpoints(endpoint: string) {
  pageRequest.endpoint = endpoint;
  loadSystemRequestLogs();
}

function loadSystemRequestLogs() {
  pageRequest.startTimeRangeBegin =
    pageRequest.startTimeRangeEnd =
    pageRequest.endTimeRangeBegin =
    pageRequest.endTimeRangeEnd =
      "";
  const startTimeRangePair = pageRequest.startTimeRange;
  if (startTimeRangePair && startTimeRangePair.length === 2) {
    pageRequest.startTimeRangeBegin = dateTimeFormat(startTimeRangePair[0]);
    pageRequest.startTimeRangeEnd = dateTimeFormat(startTimeRangePair[1]);
  }
  const endTimeRangePair = pageRequest.endTimeRange;
  if (endTimeRangePair && endTimeRangePair.length === 2) {
    pageRequest.endTimeRangeBegin = dateTimeFormat(endTimeRangePair[0]);
    pageRequest.endTimeRangeEnd = dateTimeFormat(endTimeRangePair[1]);
  }

  const { startTimeRange, endTimeRange, ...pageParams } = toRaw(pageRequest);
  axios
    .get("/kharazim-api/system/request-log/page", { params: pageParams })
    .then((response: AxiosResponse) => (requestLogPageData.value = response.data));
}

const columnShow = ref({
  endpoint: { name: "endpoint", show: true },
  uri: { name: "请求URI", show: true },
  requestParams: { name: "请求参数", show: false },
  requestBody: { name: "请求体", show: false },
  responseBody: { name: "响应体", show: true },
  userName: { name: "请求用户", show: true },
  responseStatus: { name: "状态码", show: false },
  remoteAddr: { name: "来源地址", show: false },
  forwardedFor: { name: "forwardedFor", show: false },
  realIp: { name: "realIp", show: false },
  requestHeaders: { name: "请求头", show: false },
  responseHeaders: { name: "响应头", show: false },
  startTime: { name: "开始时间", show: true },
  endTime: { name: "结束时间", show: true },
  costMills: { name: "耗时(ms)", show: true },
});

const showColumns = ref(
  Object.keys(columnShow.value).filter((key) => {
    const filedKey = key as keyof typeof columnShow.value;
    return columnShow.value[filedKey].show;
  }),
);

function handleColumnShowChange() {
  Object.keys(columnShow.value).forEach((key) => {
    const filedKey = key as keyof typeof columnShow.value;
    columnShow.value[filedKey].show = showColumns.value.includes(key);
  });
}

const detailDialogVisible = ref(false);
const dialogContent = ref("");

function showDetailDialog(detail: any) {
  dialogContent.value = detail;
  detailDialogVisible.value = true;
}

function formatDialogContent() {
  const content = dialogContent.value;
  if (content && content.length > 0) {
    try {
      dialogContent.value = JSON.stringify(JSON.parse(content), null, 2);
    } catch (ignore) {
      ElMessage({
        message: "格式化失败",
        type: "error",
      });
    }
  }
}

function copyToClipboard() {
  navigator.clipboard.writeText(dialogContent.value).then(() => {
    ElMessage({
      message: "内容已复制到剪切板",
      type: "success",
    });
  });
}

function showEndpointLogs(endpoint: string) {
  Object.assign(pageRequest, initPageRequest);
  pageRequest.endpoint = endpoint;
  loadSystemRequestLogs();
}

defineExpose({
  showEndpointLogs,
});

onMounted(() => {
  loadSystemRequestLogs();
});
</script>

<style scoped>
pre {
  background-color: #f7f7f7;
  padding: 10px;
  border-radius: 4px;
  white-space: pre-wrap;
}
</style>
