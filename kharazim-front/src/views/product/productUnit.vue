<template>
  <div>
    <div>
      <el-table
        :data="unitPageData"
        border
        style="width: 100%; margin-top: 10px"
      >
        <el-table-column label="单位编码" prop="code" />
        <el-table-column label="单位名称" prop="name" />
        <el-table-column label="单位英文名称" prop="englishName" />
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
        @size-change="loadUnit"
        @current-change="loadUnit"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";

interface ProductUnit {
  code: string;
  name: string;
  englishName: string;
}

const unitPageData = ref<ProductUnit[]>([]);
const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [10, 20, 50, 100],
});

function loadUnit() {
  axios
    .get(
      `/kharazim-api/product/unit/page?pageSize=${pageInfo.pageSize}&pageIndex=${pageInfo.currentPage}`,
    )
    .then((response: AxiosResponse) => {
      unitPageData.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
    });
}

onMounted(() => loadUnit());
</script>

<style scoped></style>