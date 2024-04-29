<template>
  <div>
    <el-form
      :inline="true"
      :model="pageRequest"
      class="page-form-block"
      @keyup.enter="loadProductPublish"
    >
      <el-form-item label="商品名称">
        <el-input
          v-model="pageRequest.skuName"
          clearable
          placeholder="商品名称"
        />
      </el-form-item>
      <el-form-item label="发布状态">
        <el-select
          v-model="pageRequest.publishStatus"
          clearable
          filterable
          placeholder="选择发布状态"
        >
          <el-option key="WAIT_EFFECT" label="待生效" value="WAIT_EFFECT" />
          <el-option key="IN_EFFECT" label="生效中" value="IN_EFFECT" />
          <el-option key="LOST_EFFECT" label="已失效" value="LOST_EFFECT" />
          <el-option key="CANCELED" label="已取消" value="CANCELED" />
        </el-select>
      </el-form-item>
      <el-form-item label="诊所">
        <el-select
          v-model="pageRequest.clinicCodes"
          clearable
          filterable
          multiple
          placeholder="选择诊所"
          reserve-keyword
        >
          <el-option
            v-for="item in clinicOptions"
            :key="item.code"
            :label="item.name"
            :value="item.code"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadProductPublish">查询</el-button>
        <el-button type="primary" @click="clearPageRequestAndLoadProductPublish"
          >重置
        </el-button>
      </el-form-item>
    </el-form>
  </div>
  <div class="pagination-block">
    <el-pagination
      v-model:current-page="pageInfo.currentPage"
      v-model:page-size="pageInfo.pageSize"
      :page-sizes="pageInfo.pageSizes"
      :total="pageInfo.totalCount"
      background
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="loadProductPublish"
      @current-change="loadProductPublish"
    />
  </div>
  <div>
    <el-table :data="skuPublishPageData" border style="width: 100%">
      <el-table-column label="发布编码" prop="code" />
      <el-table-column align="center" label="发布状态" width="120">
        <template v-slot="{ row }">
          <el-tag :type="statusTagType(row.publishStatus)" effect="dark"
            >{{ row.publishStatusName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="商品编码" prop="skuCode">
        <template v-slot="{ row }">
          <el-link
            :href="'/#/product-info?skuCode=' + row.skuCode"
            type="primary"
            >{{ row.skuCode }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column label="商品名称" prop="skuName" />
      <el-table-column align="center" label="商品主图" width="120">
        <template v-slot="{ row }">
          <el-image
            v-if="row.defaultImageUrl"
            :src="row.defaultImageUrl"
            style="width: 80px"
          >
          </el-image>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="发布价格"
        prop="price"
        width="120"
      />
      <el-table-column label="诊所名称" prop="clinicName" width="200" />
      <el-table-column label="商品分类" prop="categoryFullName" width="240" />
      <el-table-column label="供应商" prop="supplierName" width="130" />
      <el-table-column align="center" label="生效时间范围" width="200">
        <template v-slot="{ row }">
          {{ row.effectBegin + " ~ " + row.effectEnd }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="100">
        <template v-slot="{ row }">
          <el-button
            v-if="
              row.publishStatus === 'WAIT_EFFECT' ||
              row.publishStatus === 'IN_EFFECT'
            "
            plain
            size="small"
            type="danger"
            @click="cancelPublish(row)"
            >取消发布
          </el-button>
        </template>
      </el-table-column>
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
      @size-change="loadProductPublish"
      @current-change="loadProductPublish"
    />
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import axios from "@/utils/http.js";
import { AxiosResponse } from "axios";
import { ElMessage, ElMessageBox } from "element-plus";

interface SkuPublish {
  code: string;
  publishStatus: string;
  publishStatusName: string;
  skuCode: string;
  skuName: string;
  clinicCode: string;
  clinicName: string;
  categoryCode: string;
  categoryName: string;
  categoryFullName: string;
  supplierCode: string;
  supplierName: string;
  unitCode: string;
  unitName: string;
  defaultImage: string;
  defaultImageUrl: string;
  price: string;
  effectBegin: string;
  effectEnd: string;
}

const skuPublishPageData = ref<SkuPublish[]>([]);
const pageRequest = reactive({
  skuName: "",
  publishStatus: "",
  clinicCodes: [],
  description: "",
});
const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [10, 20, 50, 100],
});

function statusTagType(publishStatus: string) {
  switch (publishStatus) {
    case "WAIT_EFFECT":
      return "primary";
    case "IN_EFFECT":
      return "success";
    case "LOST_EFFECT":
      return "warning";
    case "CANCELED":
      return "info";
  }
}

function clearPageRequestAndLoadProductPublish() {
  pageRequest.skuName = "";
  pageRequest.publishStatus = "";
  pageRequest.clinicCodes = [];
  pageRequest.description = "";
  loadProductPublish();
}

function loadProductPublish() {
  axios
    .get(
      `/kharazim-api/product/publish/page?pageSize=${pageInfo.pageSize}&pageNum=${pageInfo.currentPage}`,
      {
        params: pageRequest,
      },
    )
    .then((response: AxiosResponse) => {
      skuPublishPageData.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
    });
}

function cancelPublish(row: SkuPublish) {
  ElMessageBox.confirm("确认取消发布？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      axios
        .post(`/kharazim-api/product/publish/cancel-publish/${row.code}`)
        .then(() => {
          loadProductPublish();
          ElMessage({
            showClose: true,
            message: "操作成功",
            type: "success",
          });
        });
    })
    .catch(() => {});
}

interface ClinicOption {
  code: string;
  name: string;
}

const clinicOptions = ref<ClinicOption[]>([]);

function loadClinicOptions() {
  axios.get("/kharazim-api/clinic/list").then((res: AxiosResponse) => {
    clinicOptions.value = res.data.data;
  });
}

onMounted(() => {
  loadProductPublish();
  loadClinicOptions();
});
</script>

<style scoped></style>
