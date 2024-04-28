<template>
  <div>
    <el-table :data="rolePageData" border style="width: 100%">
      <el-table-column label="角色编码" prop="code" />
      <el-table-column label="角色名" prop="name" />
      <el-table-column label="状态" prop="status">
        <template v-slot="{ row }">
          <el-switch
            v-model="row.status"
            active-value="ENABLED"
            inactive-value="DISABLED"
            @change="switchStatus(row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" />
      <el-table-column label="更新时间" prop="updateTime" />
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
      @size-change="loadRole"
      @current-change="loadRole"
    />
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import axios from "@/utils/http.js";
import { AxiosResponse } from "axios";

interface RoleData {
  id: number;
  code: string;
  name: string;
  status: string;
  createTime: string;
  updateTime: string;
}

const rolePageData = ref<RoleData[]>([]);
const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [10, 20, 50, 100],
});

function loadRole() {
  axios
    .get(
      `/kharazim-api/roles/pages?pageSize=${pageInfo.pageSize}&pageNum=${pageInfo.currentPage}`,
    )
    .then((response: AxiosResponse) => {
      rolePageData.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
    });
}

function switchStatus(role: RoleData) {
  if (role.status === "ENABLED") {
    axios.put(`/kharazim-api/roles/enable/${role.id}`).then(() => loadRole());
  }
  if (role.status === "DISABLED") {
    axios.put(`/kharazim-api/roles/disable/${role.id}`).then(() => loadRole());
  }
}

onMounted(() => loadRole());
</script>