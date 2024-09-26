<template>
  <div>
    <el-button @click="showAddSupplierDialog">新建供应商</el-button>
  </div>
  <div>
    <div>
      <el-table
        :data="supplierPageData"
        border
        style="width: 100%; margin-top: 10px"
      >
        <el-table-column label="供应商编码" prop="code" width="160" />
        <el-table-column label="供应商名称" prop="name" width="350" />
        <el-table-column label="创建人" prop="creator" width="160" />
        <el-table-column label="创建时间" prop="createTime" width="220" />
        <el-table-column label="备注" prop="remark" />
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
        @size-change="loadSupplier"
        @current-change="loadSupplier"
      />
    </div>
  </div>
  <el-dialog v-model="addSupplierVisible" title="新建供应商" width="500">
    <el-form :model="addSupplierData" label-width="20%">
      <el-form-item label="供应商名称">
        <el-input
          v-model="addSupplierData.name"
          autocomplete="off"
          placeholder="请输入供应商名称"
        />
      </el-form-item>
      <el-form-item label="备注">
        <el-input
          v-model="addSupplierData.remark"
          autocomplete="off"
          placeholder="备注"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeAddSupplierDialog">取消</el-button>
        <el-button type="primary" @click="confirmAddSupplier">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { AxiosResponse } from "axios";

const supplierPageData = ref<SupplierVO[]>([]);
const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [10, 20, 50, 100],
});

function loadSupplier() {
  axios
    .get(
      `/kharazim-api/supplier/page?pageSize=${pageInfo.pageSize}&pageIndex=${pageInfo.currentPage}`,
    )
    .then((response: AxiosResponse) => {
      supplierPageData.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
    });
}

const addSupplierVisible = ref(false);
const addSupplierData = ref<SupplierVO>();

function closeAddSupplierDialog() {
  addSupplierVisible.value = false;
}

function showAddSupplierDialog() {
  addSupplierData.value = {
    code: "",
    name: "",
    remark: "",
    creator: "",
    creatorCode: "",
    createTime: "",
  };
  addSupplierVisible.value = true;
}

function confirmAddSupplier() {
  axios
    .post("/kharazim-api/supplier/add", {
      name: addSupplierData.value?.name,
      remark: addSupplierData.value?.remark,
    })
    .then(() => {
      loadSupplier();
      closeAddSupplierDialog();
    });
}

onMounted(() => loadSupplier());
</script>

<script lang="ts">
import axios from "@/utils/http.js";

export interface SupplierVO {
  code: string;
  name: string;
  remark: string;
  creator: string;
  creatorCode: string;
  createTime: string;
}

/**
 * supplier options
 */
export async function loadSupplierOptions() {
  let res = await axios.get("/kharazim-api/supplier/list");
  return res.data.data;
}
</script>
<style scoped></style>
