<template>
  <div>
    <el-form
      :model="createPrescriptionRequest"
      label-width="auto"
      style="max-width: 600px"
    >
      <el-form-item label="诊所">
        <el-select
          v-model="createPrescriptionRequest.clinicCode"
          clearable
          filterable
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
      <el-form-item label="单价">
        <el-input-number
          v-model="createPrescriptionRequest.price"
          :controls="false"
          :max="999999"
          :min="0.01"
          :precision="2"
          :step="0.01"
          placeholder="单价"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="生效范围">
        <el-col :span="11">
          <el-date-picker
            v-model="createPrescriptionRequest.effectBegin"
            placeholder="生效时间"
            style="width: 100%"
            type="datetime"
          />
        </el-col>
        <el-col :span="2" style="text-align: center">-</el-col>
        <el-col :span="11">
          <el-date-picker
            v-model="createPrescriptionRequest.effectEnd"
            placeholder="失效时间"
            style="width: 100%"
            type="datetime"
          />
        </el-col>
      </el-form-item>
    </el-form>
  </div>
  <el-button type="primary" @click="showSelectProduct">选择商品</el-button>
  <div>
    <el-table
      :data="selectedProduct"
      border
      empty-text="请选择商品"
      style="width: 100%; margin-top: 10px"
    >
      <el-table-column label="商品编码" prop="code" width="160" />
      <el-table-column label="商品名称" prop="name" width="220" />
      <el-table-column align="center" label="主图" width="120">
        <template v-slot="{ row }">
          <el-image
            v-if="row.defaultImageUrl"
            :preview-src-list="[row.defaultImageUrl, ...row.imageUrls]"
            :src="row.defaultImageUrl"
            preview-teleported
            style="width: 90px"
          />
        </template>
      </el-table-column>
      <el-table-column label="供应商" prop="supplierName" width="130" />
      <el-table-column label="商品单位" prop="unitName" width="100" />
      <el-table-column label="商品属性" prop="attributesDesc" width="200" />
      <el-table-column label="商品分类" prop="categoryFullName" width="200" />
      <el-table-column label="描述信息" min-width="120" prop="description" />
    </el-table>

    <div style="float: right; margin-top: 15px">
      <el-button @click="cancelCreateProductPublish">取消</el-button>
      <el-button type="primary" @click="confirmCreateProductPublish">
        确定
      </el-button>
    </div>
  </div>
  <br />
  <el-dialog
    v-model="selectProductVisible"
    :close-on-click-modal="false"
    draggable
    title="选择商品"
    width="75%"
  >
    <div>
      <el-form :inline="true" :model="pageRequest" class="page-form-block">
        <el-form-item label="商品编码">
          <el-input
            v-model="pageRequest.code"
            clearable
            placeholder="商品编码"
            @keyup.enter="loadProducts"
          />
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input
            v-model="pageRequest.name"
            clearable
            placeholder="商品名称"
            @keyup.enter="loadProducts"
          />
        </el-form-item>
        <el-form-item label="商品分类">
          <el-tree-select
            v-model="pageRequest.categoryCodes"
            :data="categoryData"
            :props="{ value: 'code', label: 'name' }"
            :render-after-expand="false"
            check-on-click-node
            check-strictly
            clearable
            multiple
            node-key="code"
            placeholder="选择商品分类"
            show-checkbox
            value-key="code"
          />
        </el-form-item>
        <el-form-item label="供应商">
          <el-select
            v-model="pageRequest.supplierCodes"
            clearable
            filterable
            multiple
            placeholder="选择供应商"
            reserve-keyword
          >
            <el-option
              v-for="item in supplierOptions"
              :key="item.code"
              :label="item.name"
              :value="item.code"
            />
          </el-select>
        </el-form-item>
        <el-form-item class="page-form-block-search-block">
          <el-button type="primary" @click="loadProducts">查询</el-button>
          <el-button type="primary" @click="clearPageRequestAndLoadProducts">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div>
      <el-table
        :data="productPageData"
        border
        highlight-current-row
        style="width: 100%; margin-top: 10px"
        @current-change="changeSelected"
      >
        <el-table-column label="商品编码" prop="code" width="160" />
        <el-table-column label="商品名称" prop="name" width="220" />
        <el-table-column align="center" label="主图" width="120">
          <template v-slot="{ row }">
            <el-image
              v-if="row.defaultImageUrl"
              :preview-src-list="[row.defaultImageUrl, ...row.imageUrls]"
              :src="row.defaultImageUrl"
              preview-teleported
              style="width: 30px"
            />
          </template>
        </el-table-column>
        <el-table-column label="供应商" prop="supplierName" width="130" />
        <el-table-column label="商品单位" prop="unitName" width="100" />
        <el-table-column label="商品属性" prop="attributesDesc" width="200" />
        <el-table-column
          label="商品分类"
          min-width="200"
          prop="categoryFullName"
        />
      </el-table>
    </div>
    <div class="pagination-block-center">
      <el-pagination
        v-model:current-page="pageInfo.currentPage"
        v-model:page-size="pageInfo.pageSize"
        :page-sizes="pageInfo.pageSizes"
        :total="pageInfo.totalCount"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadProducts"
        @current-change="loadProducts"
      />
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeSelectDialog">取消</el-button>
        <el-button
          :disabled="selectedAtTable === null"
          type="primary"
          @click="confirmSelectProduct"
        >
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";
import { ElMessage, ElTable } from "element-plus";
import { dateTimeFormat } from "@/utils/DateUtil.js";
import {
  ProductCategory,
  ProductInfo,
  SupplierOption,
} from "./productInfo.vue";
import { useRouter } from "vue-router";

const router = useRouter();

interface CreateProductPublishRequest {
  skuCode: string;
  clinicCode: string;
  price: number | null;
  effectBegin: Date | null;
  effectEnd: Date | null;
}

const createPrescriptionRequest = ref<CreateProductPublishRequest>({
  skuCode: "",
  clinicCode: "",
  price: null,
  effectBegin: null,
  effectEnd: null,
});

function cancelCreateProductPublish() {
  router.push("/product-publish");
}

function confirmCreateProductPublish() {
  const effectBegin = dateTimeFormat(
    createPrescriptionRequest.value.effectBegin,
  );
  const effectEnd = dateTimeFormat(createPrescriptionRequest.value.effectEnd);
  axios
    .post("/kharazim-api/product/publish/do-publish", {
      skuCode: createPrescriptionRequest.value.skuCode,
      clinicCode: createPrescriptionRequest.value.clinicCode,
      price: createPrescriptionRequest.value.price,
      effectBegin: effectBegin,
      effectEnd: effectEnd,
    })
    .then(() => {
      ElMessage({
        showClose: true,
        message: "商品发布成功",
        type: "success",
      });
      router.push("/product-publish");
    });
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

const selectedAtTable = ref<ProductInfo | null>();

function changeSelected(row: ProductInfo) {
  selectedAtTable.value = row;
}

function closeSelectDialog() {
  selectProductVisible.value = false;
}

function confirmSelectProduct() {
  if (selectedAtTable.value) {
    selectedProduct.value = [selectedAtTable.value];
    createPrescriptionRequest.value.skuCode = selectedAtTable.value.code;
  }
  closeSelectDialog();
}

const selectedProduct = ref<ProductInfo[]>([]);

const selectProductVisible = ref(false);

function showSelectProduct() {
  pageInfo.currentPage = 1;
  pageInfo.pageSize = 10;
  pageInfo.totalCount = 0;
  pageInfo.pageSizes = [5, 10, 20, 50];
  selectedAtTable.value = null;

  loadProducts();
  selectProductVisible.value = true;
}

const pageRequest = reactive({
  code: "",
  name: "",
  categoryCodes: [],
  supplierCodes: [],
});

const productPageData = ref<ProductInfo[]>([]);
const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [5, 10, 20, 50],
});

function loadProducts() {
  axios
    .get(
      `/kharazim-api/product/sku/page?pageSize=${pageInfo.pageSize}&pageNum=${pageInfo.currentPage}`,
      {
        params: pageRequest,
      },
    )
    .then((response: AxiosResponse) => {
      productPageData.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
    });
}

function clearPageRequestAndLoadProducts() {
  pageRequest.code = "";
  pageRequest.name = "";
  pageRequest.categoryCodes = [];
  pageRequest.supplierCodes = [];
  loadProducts();
}

const supplierOptions = ref<SupplierOption[]>([]);

function loadSupplierOptions() {
  axios.get("/kharazim-api/supplier/list").then((res: AxiosResponse) => {
    supplierOptions.value = res.data.data;
  });
}

const categoryData = ref<ProductCategory[]>([]);

function loadCategories() {
  axios
    .get("/kharazim-api/product/category/tree")
    .then((res: AxiosResponse) => {
      categoryData.value = res.data.data;
    });
}

onMounted(() => {
  loadClinicOptions();
  loadSupplierOptions();
  loadCategories();
});
</script>

<style scoped>
:deep(.el-input__inner) {
  text-align: left;
}
</style>
