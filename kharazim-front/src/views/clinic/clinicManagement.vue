<template>
  <div>
    <el-form :inline="true" :model="pageRequest" class="page-condition-form">
      <el-form-item label="名称">
        <el-input v-model="pageRequest.name" clearable placeholder="名称" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="pageRequest.status" clearable placeholder="状态">
          <el-option label="正常经营" value="NORMAL" />
          <el-option label="已关闭" value="CLOSED" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadClinic">查询</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <el-table :data="clinicPageData" border style="width: 100%">
      <el-table-column label="诊所编码" prop="code" width="200" />
      <el-table-column label="诊所名称" prop="name" />
      <el-table-column label="诊所英文名称" prop="englishName" />
      <el-table-column label="状态" prop="statusName" />
    </el-table>
  </div>
  <div class="pagination-block">
    <el-pagination
      v-model:current-page="pageInfo.currentPage"
      v-model:page-size="pageInfo.pageSize"
      :page-sizes="pageInfo.pageSizes"
      :total="pageInfo.totalCount"
      background
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="loadClinic"
      @current-change="loadClinic"
    />
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import axios from "@/utils/http.js";
import { AxiosResponse } from "axios";

interface ClinicData {
  code: string;
  name: string;
  englishName: string;
  status: string;
  statusName: string;
}

const clinicPageData = ref<ClinicData[]>([]);
const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [10, 20, 50, 100],
});

const pageRequest = reactive({
  name: "",
  status: "",
});

function loadClinic() {
  axios
    .get(
      `/kharazim-api/clinic/page?pageSize=${pageInfo.pageSize}&pageNum=${pageInfo.currentPage}` +
        `${pageRequest.name ? "&name=" + pageRequest.name : ""}` +
        `${pageRequest.status ? "&status=" + pageRequest.status : ""}`,
    )
    .then((response: AxiosResponse) => {
      clinicPageData.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
    });
}

onMounted(() => loadClinic());
</script>

<style scoped>
.page-condition-form .el-input {
  --el-input-width: 220px;
}

.page-condition-form .el-select {
  --el-select-width: 220px;
}
</style>