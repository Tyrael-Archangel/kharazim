<template>
  <div>
    <el-form :inline="true" :model="pageRequest" class="page-form-block" @keyup.enter="loadSettlementOrders">
      <el-form-item label="结算单编码">
        <el-input v-model="pageRequest.settlementOrderCode" clearable placeholder="结算单编码" />
      </el-form-item>
      <el-form-item label="来源处方编码">
        <el-input v-model="pageRequest.sourcePrescriptionCode" clearable placeholder="来源处方编码" />
      </el-form-item>
      <el-form-item label="会员">
        <el-select
          v-model="pageRequest.customerCode"
          :remote-method="async (query: any) => (customers = await loadSimpleCustomers(query))"
          clearable
          filterable
          placeholder="选择会员"
          remote
          width="500px"
        >
          <el-option v-for="item in customers" :key="item.code" :label="item.name" :value="item.code">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right">{{ item.phone }}</span>
          </el-option>
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
      <el-form-item label="结算状态">
        <el-select v-model="pageRequest.status" clearable filterable placeholder="选择结算状态">
          <el-option
            v-for="option in settlementStatusOptions"
            :key="option.key"
            :label="option.value"
            :value="option.key"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadSettlementOrders">查询</el-button>
        <el-button type="primary" @click="resetAndLoadSettlementOrders"> 重置</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <div>
      <el-table :data="settlementOrderPageData.data" border style="width: 100%; margin-top: 10px">
        <el-table-column type="expand">
          <template v-slot="{ row }">
            <div style="padding: 10px 50px 10px 50px">
              <el-table :data="row.items" border>
                <el-table-column align="center" type="index" width="50" />
                <el-table-column align="center" label="商品主图" width="160">
                  <template v-slot="{ row: product }">
                    <el-image v-if="product.defaultImageUrl" :src="product.defaultImageUrl" style="width: 50px">
                    </el-image>
                  </template>
                </el-table-column>
                <el-table-column label="商品编码" prop="skuCode" />
                <el-table-column label="商品名称" prop="skuName" />
                <el-table-column label="单位" prop="unitName" width="160" />
                <el-table-column label="商品分类" prop="categoryName" />
                <el-table-column label="供应商" prop="supplierName" />
                <el-table-column align="center" label="单价" prop="price" />
                <el-table-column align="center" label="数量" prop="quantity" />
                <el-table-column align="center" label="小计" prop="amount" />
              </el-table>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="结算单编码" prop="code" width="180" />
        <el-table-column label="处方编码" width="180">
          <template v-slot="{ row }">
            <el-link :href="'/#/prescription-info?prescriptionCode=' + row.sourcePrescriptionCode" type="primary"
              >{{ row.sourcePrescriptionCode }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="会员姓名" prop="customerName" />
        <el-table-column label="诊所" prop="clinicName" />
        <el-table-column align="center" label="总金额（元）" prop="totalAmount" />
        <el-table-column label="创建时间" prop="createTime" />
        <el-table-column label="结算时间" prop="settlementTime" />
        <el-table-column align="center" label="结算状态">
          <template v-slot="{ row }">
            <el-tag v-if="row.status === 'PAID'" disable-transitions type="success">
              <el-icon>
                <CircleCheck />
              </el-icon>
              {{ row.statusName }}
            </el-tag>
            <el-tag v-else disable-transitions type="warning">
              <el-icon>
                <Warning />
              </el-icon>
              {{ row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作">
          <template v-slot="{ row }">
            <el-button v-if="row.status !== 'PAID'" link type="primary" @click="showPay(row)"> 结算</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination-block">
      <el-pagination
        v-model:current-page="pageRequest.pageIndex"
        v-model:page-size="pageRequest.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="settlementOrderPageData.totalCount"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadSettlementOrders"
        @current-change="loadSettlementOrders"
      />
    </div>
  </div>
  <el-dialog v-model="settlementOrderPayVisible" title="结算单支付" width="1000">
    <el-table ref="customerRechargeCardTableRef" :data="customerRechargeCards" @selection-change="calTotalAmount">
      <el-table-column type="selection" />
      <el-table-column label="储值单号" prop="code" />
      <el-table-column label="余额" prop="balanceAmount" />
      <el-table-column label="折扣" prop="discountPercent" />
      <el-table-column align="center" label="使用金额">
        <template v-slot="{ row }">
          <el-input-number
            v-model="row.useAmount"
            :controls="false"
            :disabled="row.disabled"
            :max="row.balanceAmount"
            :min="0.01"
            :precision="2"
            @change="calDeductAmount(row)"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="抵扣金额">
        <template v-slot="{ row }">
          <el-input-number
            v-model="row.deductAmount"
            :controls="false"
            :disabled="row.disabled"
            :max="maxDeductAmount(row.balanceAmount, row.discountPercent)"
            :min="0.01"
            :precision="2"
            @change="calUseAmount(row)"
          ></el-input-number>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 20px">
      <el-descriptions>
        <el-descriptions-item label="总金额">
          {{ currentPaySO.totalAmount }}
        </el-descriptions-item>
        <el-descriptions-item label="总使用金额">
          <el-text :style="currentPaySO.totalAmount !== currentPaySO.totalDeductAmount ? 'color: red' : ''">
            {{ currentPaySO.totalUseAmount }}
          </el-text>
        </el-descriptions-item>
        <el-descriptions-item label="总抵扣金额">
          <el-text :style="currentPaySO.totalAmount !== currentPaySO.totalDeductAmount ? 'color: red' : ''">
            {{ currentPaySO.totalDeductAmount }}
          </el-text>
        </el-descriptions-item>
      </el-descriptions>
    </div>
    <div style="text-align: right">
      <div>
        <el-button type="primary" @click="submitPay">确认</el-button>
        <el-button @click="closePay">取消</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, toRaw } from "vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";
import { CircleCheck, Warning } from "@element-plus/icons-vue";
import Decimal from "decimal.js";
import { ElMessage, ElTable } from "element-plus";
import { ClinicVO, loadClinicOptions } from "@/views/clinic/clinicManagement.vue";
import { loadSimpleCustomers, SimpleCustomer } from "@/views/customer/customer-list";
import { DictOption, loadDictOptions } from "@/views/dict/dict-item";

interface SettlementOrderItem {
  skuCode: string;
  skuName: string;
  categoryCode: string;
  categoryName: string;
  supplierCode: string;
  supplierName: string;
  unitCode: string;
  unitName: string;
  defaultImageUrl: string;
  description: string;
  quantity: number;
  price: number;
  amount: number;
}

interface SettlementOrder {
  code: string;
  status: string;
  customerCode: string;
  customerName: string;
  clinicCode: string;
  clinicName: string;
  sourcePrescriptionCode: string;
  totalAmount: number;
  createTime: string;
  settlementTime: string;
  items: SettlementOrderItem[];
}

const settlementOrderPageData = ref({
  totalCount: 0,
  data: [] as SettlementOrder[],
});

const initPageRequest = {
  settlementOrderCode: "",
  sourcePrescriptionCode: "",
  customerCode: "",
  clinicCodes: [],
  status: "",
  pageSize: 10,
  pageIndex: 1,
};

const pageRequest = reactive({ ...initPageRequest });

function loadSettlementOrders() {
  axios.get("/kharazim-api/settlement-order/page", { params: toRaw(pageRequest) }).then((response: AxiosResponse) => {
    settlementOrderPageData.value = response.data;
  });
}

function resetAndLoadSettlementOrders() {
  Object.assign(pageRequest, initPageRequest);
  loadSettlementOrders();
}

const settlementStatusOptions = ref<DictOption[]>([]);
const customers = ref<SimpleCustomer[]>([]);
const clinicOptions = ref<ClinicVO[]>([]);

const customerRechargeCardTableRef = ref<InstanceType<typeof ElTable>>();
const settlementOrderPayVisible = ref(false);

interface CustomerRechargeCard {
  code: string;
  customerCode: string;
  customerName: string;
  balanceAmount: number;
  discountPercent: number;
  useAmount: number;
  deductAmount: number;
  disabled: boolean;
}

const customerRechargeCards = ref<CustomerRechargeCard[]>([]);

interface CurrentPaySettlementOrder {
  code: string;
  customerCode: string;
  customerName: string;
  clinicCode: string;
  clinicName: string;
  totalAmount: number;
  totalUseAmount: number;
  totalDeductAmount: number;
}

const currentPaySO = ref<CurrentPaySettlementOrder>({
  code: "",
  customerCode: "",
  customerName: "",
  clinicCode: "",
  clinicName: "",
  totalAmount: 0,
  totalUseAmount: 0,
  totalDeductAmount: 0,
});

function showPay(row: SettlementOrder) {
  currentPaySO.value = {
    code: row.code,
    customerCode: row.customerCode,
    customerName: row.customerName,
    clinicCode: row.clinicCode,
    clinicName: row.clinicName,
    totalAmount: row.totalAmount,
    totalUseAmount: 0,
    totalDeductAmount: 0,
  };
  customerRechargeCards.value = [];
  loadCustomerRechargeCards();
  settlementOrderPayVisible.value = true;
}

function loadCustomerRechargeCards() {
  let customerCode = currentPaySO.value.customerCode;
  axios.get(`/kharazim-api/recharge-card/list-effective/${customerCode}`).then((res: AxiosResponse) => {
    for (const customerRechargeCard of res.data.data) {
      customerRechargeCard.disabled = true;
    }
    customerRechargeCards.value = res.data.data;
  });
}

function calDeductAmount(row: CustomerRechargeCard) {
  console.log(row);
  const useAmount = new Decimal(row.useAmount);
  const discount = new Decimal(row.discountPercent / 100);
  const deductAmount = useAmount.dividedBy(discount);
  row.deductAmount = +deductAmount.toFixed(2);
  calTotalAmount();
}

function calUseAmount(row: CustomerRechargeCard) {
  const deductAmount = new Decimal(row.deductAmount);
  const discount = new Decimal(row.discountPercent / 100);
  const useAmount = deductAmount.mul(discount);
  row.useAmount = +useAmount.toFixed(2);

  calTotalAmount();
}

function calTotalAmount() {
  let totalUseAmount = 0;
  let totalDeductAmount = 0;
  const selectedCodes = [] as string[];
  const selectionRows = customerRechargeCardTableRef.value?.getSelectionRows() as CustomerRechargeCard[];
  for (const customerRechargeCard of selectionRows) {
    customerRechargeCard.disabled = false;
    selectedCodes.push(customerRechargeCard.code);
    if (customerRechargeCard.useAmount) {
      totalUseAmount = totalUseAmount + customerRechargeCard.useAmount;
    }
    if (customerRechargeCard.deductAmount) {
      totalDeductAmount = totalDeductAmount + customerRechargeCard.deductAmount;
    }
  }
  for (const customerRechargeCard of customerRechargeCards.value) {
    if (!selectedCodes.includes(customerRechargeCard.code)) {
      customerRechargeCard.disabled = true;
    }
  }
  currentPaySO.value.totalUseAmount = +totalUseAmount.toFixed(2);
  currentPaySO.value.totalDeductAmount = +totalDeductAmount.toFixed(2);
}

function maxDeductAmount(amount: number, discountPercent: number) {
  if (amount && discountPercent) {
    const useAmount = new Decimal(amount);
    const discount = new Decimal(discountPercent / 100);
    return +useAmount.dividedBy(discount).toFixed(2);
  }
}

function closePay() {
  settlementOrderPayVisible.value = false;
}

function submitPay() {
  if (currentPaySO.value.totalDeductAmount !== currentPaySO.value.totalAmount) {
    ElMessage.error("总抵扣金额与结算单总金额不一致!");
    return;
  }
  const rechargeCardPayDetails = [];
  const usedCustomerRechargeCards = customerRechargeCardTableRef.value?.getSelectionRows() as CustomerRechargeCard[];
  for (const customerRechargeCard of usedCustomerRechargeCards) {
    if (customerRechargeCard.useAmount && customerRechargeCard.deductAmount) {
      rechargeCardPayDetails.push({
        rechargeCardCode: customerRechargeCard.code,
        useAmount: customerRechargeCard.useAmount,
        deductAmount: customerRechargeCard.deductAmount,
      });
    }
  }
  axios
    .post("/kharazim-api/settlement-order/pay", {
      settlementOrderCode: currentPaySO.value.code,
      rechargeCardPayDetails: rechargeCardPayDetails,
    })
    .then(() => {
      ElMessage.success("操作成功");
      loadSettlementOrders();
      closePay();
    });
}

onMounted(async () => {
  settlementStatusOptions.value = await loadDictOptions("settlement_order_status");
  clinicOptions.value = await loadClinicOptions();
  loadSettlementOrders();
});
</script>

<style scoped></style>
