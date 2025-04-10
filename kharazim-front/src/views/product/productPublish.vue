<template>
  <div>
    <el-form :inline="true" :model="pageRequest" class="page-form-block" @keyup.enter="loadProductPublish">
      <el-form-item label="商品名称">
        <el-input v-model="pageRequest.skuName" clearable placeholder="商品名称" />
      </el-form-item>
      <el-form-item label="商品编码">
        <el-input v-model="pageRequest.skuCode" clearable placeholder="商品编码" />
      </el-form-item>
      <el-form-item label="发布状态">
        <el-select v-model="pageRequest.publishStatus" clearable filterable placeholder="选择发布状态">
          <el-option
            v-for="option in publishStatusOptions"
            :key="option.key"
            :label="option.value"
            :value="option.key"
          />
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
          <el-option v-for="item in clinicOptions" :key="item.code" :label="item.name" :value="item.code" />
        </el-select>
      </el-form-item>
      <el-form-item class="page-form-block-search-block">
        <el-button type="primary" @click="loadProductPublish">查询</el-button>
        <el-button type="primary" @click="clearPageRequestAndLoadProductPublish"> 重置</el-button>
      </el-form-item>
    </el-form>
  </div>
  <router-link to="/create-product-publish">
    <el-button type="primary">发布商品</el-button>
  </router-link>
  <div class="pagination-block">
    <el-pagination
      v-model:current-page="pageRequest.pageIndex"
      v-model:page-size="pageRequest.pageSize"
      :page-sizes="[10, 20, 50, 100]"
      :total="skuPublishPageData.totalCount"
      background
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="loadProductPublish"
      @current-change="loadProductPublish"
    />
  </div>
  <div>
    <el-table :data="skuPublishPageData.data" border style="width: 100%">
      <el-table-column label="发布编码" prop="code" width="220" />
      <el-table-column align="center" label="发布状态" width="100">
        <template v-slot="{ row }">
          <el-tag :type="statusTagType(row.publishStatus)" effect="dark">{{ row.publishStatusName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="商品编码" prop="skuCode" width="150">
        <template v-slot="{ row }">
          <el-link type="primary" @click="router.push(`product-info?skuCode=${row.skuCode}`)">
            {{ row.skuCode }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column label="商品名称" prop="skuName" width="160" />
      <el-table-column align="center" label="商品主图" width="110">
        <template v-slot="{ row }">
          <el-image v-if="row.defaultImageUrl" :src="row.defaultImageUrl" style="width: 80px" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="发布价格" prop="price" width="120" />
      <el-table-column label="诊所名称" prop="clinicName" width="180" />
      <el-table-column label="商品分类" prop="categoryFullName" width="220" />
      <el-table-column label="供应商" prop="supplierName" width="130" />
      <el-table-column align="center" label="生效时间范围" min-width="200">
        <template v-slot="{ row }">
          {{ row.effectBegin + " ~ " + row.effectEnd }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="100">
        <template v-slot="{ row }">
          <el-button
            v-if="row.publishStatus === 'WAIT_EFFECT' || row.publishStatus === 'IN_EFFECT'"
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
      v-model:current-page="pageRequest.pageIndex"
      v-model:page-size="pageRequest.pageSize"
      :page-sizes="[10, 20, 50, 100]"
      :total="skuPublishPageData.totalCount"
      background
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="loadProductPublish"
      @current-change="loadProductPublish"
    />
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, toRaw } from "vue";
import axios from "@/utils/http.js";
import { AxiosResponse } from "axios";
import { ElMessage, ElMessageBox } from "element-plus";
import { ClinicVO, loadClinics } from "@/views/clinic/clinicManagement.vue";
import { DictOption, loadDictOptions } from "@/views/dict/dict-item";
import { useRouter } from "vue-router";
import { fileUrl } from "@/utils/fileUrl.ts";

const router = useRouter();

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

const skuPublishPageData = ref({ totalCount: 0, data: [] as SkuPublish[] });

const initPageRequest = {
  skuName: "",
  skuCode: "",
  publishStatus: "",
  clinicCodes: [],
  description: "",
  pageSize: 10,
  pageIndex: 1,
};

const pageRequest = reactive({ ...initPageRequest });

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
  Object.assign(pageRequest, initPageRequest);
  loadProductPublish();
}

function loadProductPublish() {
  axios.get("/kharazim-api/product/publish/page", { params: toRaw(pageRequest) }).then((response: AxiosResponse) => {
    response.data.data.forEach((item: SkuPublish) => {
      item.defaultImageUrl = fileUrl(item.defaultImage);
    });
    skuPublishPageData.value = response.data;
  });
}

function cancelPublish(row: SkuPublish) {
  ElMessageBox.confirm("确认取消发布？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      axios.post(`/kharazim-api/product/publish/cancel-publish/${row.code}`).then(() => {
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

const publishStatusOptions = ref<DictOption[]>([]);
const clinicOptions = ref<ClinicVO[]>([]);

onMounted(() => {
  loadDictOptions("sku_publish_status").then((res: DictOption[]) => (publishStatusOptions.value = res));
  loadClinics().then((res: ClinicVO[]) => (clinicOptions.value = res));
  loadProductPublish();
});
</script>

<style scoped></style>
