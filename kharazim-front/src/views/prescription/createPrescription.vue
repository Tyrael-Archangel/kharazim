<template>
  <div class="create-prescription-header">
    <div class="create-prescription-header-item">
      <el-form-item label="会员">
        <el-select
          v-model="createPrescriptionRequest.customerCode"
          :remote-method="loadCustomers"
          filterable
          placeholder="选择会员"
          remote
        >
          <el-option
            v-for="item in customers"
            :key="item.code"
            :label="item.name"
            :value="item.code"
          >
            <span style="float: left">{{ item.name }}</span>
            <span class="customer-select">{{ item.phone }}</span>
          </el-option>
        </el-select>
      </el-form-item>
    </div>
    <div class="create-prescription-header-item">
      <el-form-item label="诊所">
        <el-select
          v-model="createPrescriptionRequest.clinicCode"
          clearable
          filterable
          placeholder="选择诊所"
          reserve-keyword
          @change="clearSelectedProducts"
        >
          <el-option
            v-for="item in clinicOptions"
            :key="item.code"
            :label="item.name"
            :value="item.code"
          />
        </el-select>
      </el-form-item>
    </div>
  </div>
  <el-form-item
    class="create-prescription-header-item"
    label="备注"
    style="width: 50%"
  >
    <el-input
      v-model="createPrescriptionRequest.remark"
      placeholder="备注信息"
      type="textarea"
    ></el-input>
  </el-form-item>
  <el-button type="primary" @click="showSelectProduct">选择商品</el-button>
  <div>
    <el-table
      :data="selectedProducts"
      border
      style="width: 100%; margin-top: 10px"
    >
      <el-table-column align="center" type="index" width="60" />
      <el-table-column align="center" label="操作" width="80">
        <template v-slot="{ row }">
          <el-button
            :icon="Delete"
            circle
            type="danger"
            @click="removeSelectedSku(row)"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="商品主图" width="160">
        <template v-slot="{ row }">
          <el-image
            v-if="row.defaultImageUrl"
            :src="row.defaultImageUrl"
            style="width: 80px"
          >
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="商品编码" prop="skuCode" width="160" />
      <el-table-column label="商品名称" prop="skuName" width="180" />
      <el-table-column align="center" label="单位" prop="unitName" width="80" />
      <el-table-column align="center" label="单价" prop="price" width="100" />
      <el-table-column
        align="center"
        label="可用库存"
        prop="inventoryQuantity"
        width="100"
      />
      <el-table-column align="center" label="数量" prop="quantity" width="150">
        <template v-slot="{ row }">
          <el-input-number
            v-model="row.quantity"
            :max="row.inventoryQuantity"
            min="1"
            style="width: 120px"
          ></el-input-number>
        </template>
      </el-table-column>
      <el-table-column align="center" label="金额小计" width="140">
        <template v-slot="{ row }">
          <el-text>{{ calculateItemAmount(row) }}</el-text>
        </template>
      </el-table-column>
      <el-table-column label="供应商" min-width="180" prop="supplierName" />
      <el-table-column
        label="商品分类"
        min-width="240"
        prop="categoryFullName"
      />
    </el-table>
  </div>
  <br />
  <el-button
    style="width: 8%; float: right"
    type="primary"
    @click="submitCreatePrescription"
    >提交
  </el-button>
  <el-dialog
    v-model="selectProductVisible"
    :close-on-click-modal="false"
    draggable
    title="选择商品"
    width="1400"
  >
    <div>
      <el-form
        :inline="true"
        :model="pagePublishSkuRequest"
        class="page-form-block"
        @keyup.enter="loadProductPublish"
      >
        <el-form-item label="商品编码">
          <el-input
            v-model="pagePublishSkuRequest.skuCode"
            clearable
            placeholder="商品编码"
          />
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input
            v-model="pagePublishSkuRequest.skuName"
            clearable
            placeholder="商品名称"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadProductPublish">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      ref="selectSkuTableRef"
      :data="publishSkuPage"
      border
      row-key="code"
      style="width: 100%; margin-top: 10px"
    >
      <el-table-column
        :selectable="skuSelectable"
        type="selection"
        width="50"
      />
      <el-table-column label="商品编码" prop="skuCode" width="180" />
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
      <el-table-column label="商品名称" prop="skuName" width="260" />
      <el-table-column label="诊所" prop="clinicName" width="160" />
      <el-table-column label="可用库存" prop="inventoryQuantity" width="120" />
      <el-table-column align="center" label="价格" prop="price" width="120" />
      <el-table-column label="商品分类" prop="categoryFullName" width="240" />
      <el-table-column label="供应商" prop="supplierName" />
    </el-table>
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

const router = useRouter();

interface Product {
  skuCode: string;
  quantity: number;
}

interface CreatePrescriptionRequest {
  customerCode: string;
  clinicCode: string;
  remark: string;
  products: Product[];
}

const createPrescriptionRequest = ref<CreatePrescriptionRequest>({
  customerCode: "",
  clinicCode: "",
  remark: "",
  products: [],
});

interface Customer {
  code: string;
  name: string;
  phone: string;
}

const customers = ref<Customer[]>([]);

function loadCustomers(query: string) {
  axios
    .get(`/kharazim-api/customer/list?conditionType=NAME&keyword=${query}`)
    .then((res: AxiosResponse) => {
      customers.value = res.data.data;
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
  price: number;
  effectBegin: string;
  effectEnd: string;
  quantity: number;
  inventoryQuantity: number;
}

const selectedProducts = ref<SkuPublish[]>([]);

function clearSelectedProducts() {
  selectedProducts.value = [] as SkuPublish[];
}

const selectProductVisible = ref(false);

function showSelectProduct() {
  const clinicCode = createPrescriptionRequest.value.clinicCode;
  if (!clinicCode) {
    ElMessage.error("请选择诊所");
    return;
  }
  currentSelectedSkus.value = [];
  for (const selectedProduct of selectedProducts.value) {
    currentSelectedSkus.value.push(selectedProduct);
  }

  pagePublishSkuRequest.clinicCodes = [clinicCode];
  pagePublishSkuRequest.skuName = "";
  pageInfo.currentPage = 1;
  pageInfo.pageSize = 10;
  pageInfo.totalCount = 0;
  pageInfo.pageSizes = [5, 10, 20, 50];

  loadProductPublish();
  selectProductVisible.value = true;
}

function removeSelectedSku(row: SkuPublish) {
  selectedProducts.value = selectedProducts.value.filter(
    (e) => e.code !== row.code,
  );
}

const selectSkuTableRef = ref<InstanceType<typeof ElTable>>();

const pagePublishSkuRequest = reactive({
  skuCode: "",
  skuName: "",
  publishStatus: "IN_EFFECT",
  clinicCodes: [] as string[],
});

const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [5, 10, 20, 50],
});
const publishSkuPage = ref<SkuPublish[]>([]);
const currentSelectedSkus = ref<SkuPublish[]>([]);

function loadProductPublish() {
  if (selectSkuTableRef.value) {
    const selectionRows =
      selectSkuTableRef.value.getSelectionRows() as SkuPublish[];
    currentSelectedSkus.value.push(...selectionRows);
  }
  axios
    .get(
      `/kharazim-api/product/publish/page?pageSize=${pageInfo.pageSize}&pageIndex=${pageInfo.currentPage}`,
      {
        params: pagePublishSkuRequest,
      },
    )
    .then((response: AxiosResponse) => {
      publishSkuPage.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
      querySkuInventory();
      showSelectedSku();
    });
}

function querySkuInventory() {
  const publishSkuCurrentPageData = publishSkuPage.value;
  if (publishSkuCurrentPageData) {
    const skuCodes = publishSkuCurrentPageData.map((x) => x.skuCode);
    axios
      .get(`/kharazim-api/inventory/list-clinic`, {
        params: {
          clinicCode: createPrescriptionRequest.value.clinicCode,
          skuCodes: skuCodes,
        },
      })
      .then((response) => {
        const skuUsableQuantity = new Map(
          response.data.data.map((inventory) => [
            inventory.skuCode,
            inventory.usableQuantity,
          ]),
        );
        publishSkuCurrentPageData.forEach((e) => {
          e.inventoryQuantity = skuUsableQuantity.get(e.skuCode) || 0;
        });
      });
  }
}

function skuSelectable(row: SkuPublish) {
  return row.inventoryQuantity && row.inventoryQuantity > 0;
}

function showSelectedSku() {
  nextTick(() => {
    const selectedPublishCodes = currentSelectedSkus.value.map(
      (x) => x.code,
    ) as string[];
    for (const sku of publishSkuPage.value) {
      if (selectedPublishCodes.includes(sku.code)) {
        selectSkuTableRef.value!.toggleRowSelection(sku, true);
      }
    }
  });
}

function confirmSelectSku() {
  const selectionRows =
    selectSkuTableRef.value!.getSelectionRows() as SkuPublish[];
  currentSelectedSkus.value.push(...selectionRows);
  const selectedPublishCodes = selectedProducts.value.map(
    (x) => x.code,
  ) as string[];
  for (const selectionRow of currentSelectedSkus.value) {
    if (!selectedPublishCodes.includes(selectionRow.code)) {
      if (!selectionRow.quantity) {
        selectionRow.quantity = 1;
      }
      selectedProducts.value.push(selectionRow);
    }
  }
  selectProductVisible.value = false;
}

function calculateItemAmount(row: SkuPublish) {
  const quantity = new Decimal(row.quantity);
  const price = new Decimal(row.price);
  return quantity.mul(price).toFixed(2);
}

function submitCreatePrescription() {
  let canSubmit =
    createPrescriptionRequest.value.customerCode &&
    createPrescriptionRequest.value.clinicCode &&
    selectedProducts.value &&
    selectedProducts.value.length > 0;
  if (!canSubmit) {
    ElMessage.error("请完善处方");
    return;
  }
  const products = selectedProducts.value.map((x) => {
    return { skuCode: x.skuCode, quantity: x.quantity };
  });

  const createPrescriptionParam = {
    customerCode: createPrescriptionRequest.value.customerCode,
    clinicCode: createPrescriptionRequest.value.clinicCode,
    remark: createPrescriptionRequest.value.remark,
    products: products,
  };
  axios
    .post("/kharazim-api/prescription/create", createPrescriptionParam)
    .then(() => {
      ElMessage({
        showClose: true,
        message: "创建处方成功",
        type: "success",
      });
      router.push("/prescription-info");
    });
}

onMounted(() => loadClinicOptions());
</script>

<style scoped>
.customer-select {
  float: right;
  color: var(--el-text-color-secondary);
  font-size: 13px;
}

.create-prescription-header {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  padding-bottom: 15px;
  width: 100%;
}

.create-prescription-header-item {
  width: 24%;
  padding-left: 20px;
  padding-right: 20px;
}
</style>
