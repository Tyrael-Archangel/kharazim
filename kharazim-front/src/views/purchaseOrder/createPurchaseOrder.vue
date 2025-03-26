<template>
  <div class="create-purchase-order-header">
    <div class="create-purchase-order-header-item">
      <el-form-item label="诊所">
        <el-select
          v-model="createPurchaseOrderRequest.clinicCode"
          clearable
          filterable
          placeholder="选择诊所"
          reserve-keyword
        >
          <el-option v-for="item in clinicOptions" :key="item.code" :label="item.name" :value="item.code" />
        </el-select>
      </el-form-item>
    </div>
    <div class="create-purchase-order-header-item">
      <el-form-item label="供应商">
        <el-select
          v-model="createPurchaseOrderRequest.supplierCode"
          clearable
          filterable
          placeholder="选择供应商"
          reserve-keyword
          @change="clearSelectedProducts"
        >
          <el-option v-for="item in supplierOptions" :key="item.code" :label="item.name" :value="item.code" />
        </el-select>
      </el-form-item>
    </div>
  </div>
  <el-form-item class="create-purchase-order-header-item" label="备注" style="width: 50%">
    <el-input v-model="createPurchaseOrderRequest.remark" placeholder="备注信息" type="textarea"></el-input>
  </el-form-item>
  <el-button type="primary" @click="showSelectProduct">选择商品</el-button>
  <div>
    <el-table :data="selectedProducts" border style="width: 100%; margin-top: 10px">
      <el-table-column align="center" type="index" width="90" />
      <el-table-column align="center" label="商品主图" width="160">
        <template v-slot="{ row }">
          <el-image v-if="row.defaultImageUrl" :src="row.defaultImageUrl" style="width: 80px"></el-image>
        </template>
      </el-table-column>
      <el-table-column label="商品编码" prop="code" width="160" />
      <el-table-column label="商品名称" prop="name" width="200" />
      <el-table-column align="center" label="单位" prop="unitName" width="100" />
      <el-table-column align="center" label="单价" prop="price" width="130">
        <template v-slot="{ row }">
          <el-input-number
            v-model="row.price"
            :controls="false"
            :max="9999.99"
            :min="0.01"
            :precision="2"
            :step="0.01"
            style="width: 100px"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="数量" prop="quantity" width="180">
        <template v-slot="{ row }">
          <el-input-number v-model="row.quantity" :max="999" :min="1" style="width: 120px" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="金额小计" width="150">
        <template v-slot="{ row }">
          <el-text>{{ calculateItemAmount(row) }}</el-text>
        </template>
      </el-table-column>
      <el-table-column label="商品分类" min-width="200" prop="categoryFullName" />
      <el-table-column align="center" label="操作">
        <template v-slot="{ row }">
          <el-button :icon="Delete" circle type="danger" @click="removeSelectedSku(row)" />
        </template>
      </el-table-column>
    </el-table>
  </div>
  <br />
  <el-button style="width: 8%; float: right" type="primary" @click="submitCreatePurchaseOrder">提交</el-button>
  <el-dialog v-model="selectProductVisible" :close-on-click-modal="false" draggable title="选择商品" width="1400">
    <div>
      <el-form :inline="true" :model="pageProductSkuRequest" class="page-form-block" @keyup.enter="loadProductSku">
        <el-form-item label="商品编码">
          <el-input v-model="pageProductSkuRequest.code" clearable placeholder="商品编码" />
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input v-model="pageProductSkuRequest.name" clearable placeholder="商品名称" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadProductSku">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      ref="selectSkuTableRef"
      :data="productSkuPage"
      border
      row-key="code"
      style="width: 100%; margin-top: 10px"
    >
      <el-table-column type="selection" width="50" />
      <el-table-column label="商品编码" prop="code" width="180" />
      <el-table-column label="商品名称" prop="name" width="260" />
      <el-table-column align="center" label="商品主图" width="120">
        <template v-slot="{ row }">
          <el-image v-if="row.defaultImageUrl" :src="row.defaultImageUrl" style="width: 80px"></el-image>
        </template>
      </el-table-column>
      <el-table-column label="商品单位" prop="unitName" width="100" />
      <el-table-column label="属性" prop="attributesDesc" width="220" />
      <el-table-column label="商品分类" min-width="240" prop="categoryFullName" />
    </el-table>
    <div class="pagination-block">
      <el-pagination
        v-model:current-page="pageInfo.currentPage"
        v-model:page-size="pageInfo.pageSize"
        :page-sizes="pageInfo.pageSizes"
        :total="pageInfo.totalCount"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadProductSku"
        @current-change="loadProductSku"
      />
    </div>
    <div style="text-align: center; padding: 10px">
      <el-button type="primary" @click="confirmSelectSku">确定</el-button>
    </div>
  </el-dialog>
</template>

<script lang="ts" setup>
import { nextTick, onMounted, reactive, ref } from "vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";
import { ElMessage, ElTable } from "element-plus";
import { Delete } from "@element-plus/icons-vue";
import Decimal from "decimal.js";
import { useRouter } from "vue-router";
import { loadSuppliers, SupplierVO } from "@/views/supplier/supplier.vue";
import { ClinicVO, loadClinics } from "@/views/clinic/clinicManagement.vue";
import { fileUrl } from "@/utils/fileUrl.ts";

const router = useRouter();

interface PurchaseOrderItem {
  skuCode: string;
  quantity: number;
  price: number;
}

interface CreatePurchaseOrderRequest {
  clinicCode: string;
  supplierCode: string;
  remark: string;
  items: PurchaseOrderItem[];
}

const createPurchaseOrderRequest = ref<CreatePurchaseOrderRequest>({
  clinicCode: "",
  supplierCode: "",
  remark: "",
  items: [],
});

const clinicOptions = ref<ClinicVO[]>([]);
const supplierOptions = ref<SupplierVO[]>([]);

interface ProductSku {
  code: string;
  name: string;
  quantity: number;
  price: number;
  categoryCode: string;
  categoryName: string;
  categoryFullName: string;
  supplierCode: string;
  supplierName: string;
  unitCode: string;
  unitName: string;
  defaultImage: string;
  defaultImageUrl: string;
  description: string;
  attributesDesc: string;
}

const selectedProducts = ref<ProductSku[]>([]);

function clearSelectedProducts() {
  selectedProducts.value = [] as ProductSku[];
}

const selectProductVisible = ref(false);

function showSelectProduct() {
  const supplierCode = createPurchaseOrderRequest.value.supplierCode;
  if (!supplierCode) {
    ElMessage.error("请选择供应商");
    return;
  }
  currentSelectedSkus.value = [];
  for (const selectedProduct of selectedProducts.value) {
    currentSelectedSkus.value.push(selectedProduct);
  }

  pageProductSkuRequest.supplierCodes = [supplierCode];
  pageProductSkuRequest.name = "";
  pageInfo.currentPage = 1;
  pageInfo.pageSize = 10;
  pageInfo.totalCount = 0;
  pageInfo.pageSizes = [5, 10, 20, 50];

  loadProductSku();
  selectProductVisible.value = true;
}

function removeSelectedSku(row: ProductSku) {
  selectedProducts.value = selectedProducts.value.filter((e) => e.code !== row.code);
}

const selectSkuTableRef = ref<InstanceType<typeof ElTable>>();

const pageProductSkuRequest = reactive({
  code: "",
  name: "",
  categoryCodes: [] as string[],
  supplierCodes: [] as string[],
});

const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [5, 10, 20, 50],
});
const productSkuPage = ref<ProductSku[]>([]);
const currentSelectedSkus = ref<ProductSku[]>([]);

function loadProductSku() {
  if (selectSkuTableRef.value) {
    const selectionRows = selectSkuTableRef.value.getSelectionRows() as ProductSku[];
    currentSelectedSkus.value.push(...selectionRows);
  }
  axios
    .get(`/kharazim-api/product/sku/page?pageSize=${pageInfo.pageSize}&pageIndex=${pageInfo.currentPage}`, {
      params: pageProductSkuRequest,
    })
    .then((response: AxiosResponse) => {
      response.data.data.forEach((item: ProductSku) => {
        item.defaultImageUrl = fileUrl(item.defaultImage);
      });
      productSkuPage.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
      showSelectedSku();
    });
}

function showSelectedSku() {
  nextTick(() => {
    const selectedProductCodes = currentSelectedSkus.value.map((x) => x.code) as string[];
    for (const sku of productSkuPage.value) {
      if (selectedProductCodes.includes(sku.code)) {
        selectSkuTableRef.value!.toggleRowSelection(sku, true);
      }
    }
  });
}

function confirmSelectSku() {
  const selectionRows = selectSkuTableRef.value!.getSelectionRows() as ProductSku[];
  currentSelectedSkus.value.push(...selectionRows);
  const selectedProductCodes = selectedProducts.value.map((x) => x.code) as string[];
  for (const selectionRow of currentSelectedSkus.value) {
    if (!selectedProductCodes.includes(selectionRow.code)) {
      if (!selectionRow.quantity) {
        selectionRow.quantity = 1;
      }
      selectedProducts.value.push(selectionRow);
    }
  }
  selectProductVisible.value = false;
}

function calculateItemAmount(row: ProductSku) {
  if (row.price && row.quantity) {
    const quantity = new Decimal(row.quantity);
    const price = new Decimal(row.price);
    return quantity.mul(price).toFixed(2);
  } else {
    return "-";
  }
}

function submitCreatePurchaseOrder() {
  let canSubmit =
    createPurchaseOrderRequest.value.supplierCode &&
    createPurchaseOrderRequest.value.clinicCode &&
    selectedProducts.value &&
    selectedProducts.value.length > 0;
  if (!canSubmit) {
    ElMessage.error("请完善采购单");
    return;
  }
  const products = selectedProducts.value.map((x) => {
    return { skuCode: x.code, quantity: x.quantity, price: x.price };
  });

  const createPurchaseOrderParam = {
    clinicCode: createPurchaseOrderRequest.value.clinicCode,
    supplierCode: createPurchaseOrderRequest.value.supplierCode,
    remark: createPurchaseOrderRequest.value.remark,
    items: products,
  };
  axios.post("/kharazim-api/purchase-order/create", createPurchaseOrderParam).then(() => {
    ElMessage({
      showClose: true,
      message: "创建采购单成功",
      type: "success",
    });
    router.push("/purchase-order");
  });
}

onMounted(() => {
  loadClinics().then((res: ClinicVO[]) => (clinicOptions.value = res));
  loadSuppliers().then((res: SupplierVO[]) => (supplierOptions.value = res));
});
</script>

<style scoped>
.create-purchase-order-header {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  padding-bottom: 15px;
  width: 100%;
}

.create-purchase-order-header-item {
  width: 24%;
  padding-left: 20px;
  padding-right: 20px;
}
</style>
