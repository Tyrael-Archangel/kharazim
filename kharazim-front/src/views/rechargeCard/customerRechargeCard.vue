<template>
  <div>
    <el-form :inline="true" :model="pageRequest" class="page-form-block" @keyup.enter="loadCustomerRechargeCard">
      <el-form-item label="储值单号">
        <el-input v-model="pageRequest.code" clearable placeholder="储值单号" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="pageRequest.statuses" clearable multiple placeholder="状态">
          <el-option v-for="option in statusOptions" :key="option.key" :label="option.value" :value="option.key" />
        </el-select>
      </el-form-item>
      <el-form-item label="会员">
        <el-select
          v-model="pageRequest.customerCode"
          :remote-method="async (query: any) => (customers = await loadSimpleCustomers(query))"
          clearable
          filterable
          placeholder="选择会员"
          remote
        >
          <el-option v-for="item in customers" :key="item.code" :label="item.name" :value="item.code">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right">{{ item.phone }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="储值卡项">
        <el-select
          v-model="pageRequest.rechargeCardTypes"
          clearable
          filterable
          multiple
          placeholder="选择储值卡项"
          reserve-keyword
        >
          <el-option v-for="item in rechargeCardTypeOptions" :key="item.code" :label="item.name" :value="item.code" />
        </el-select>
      </el-form-item>
      <el-form-item label="成交员工">
        <el-select
          v-model="pageRequest.traderUserCode"
          :remote-method="async (query: any) => (traderUsers = await loadSimpleUsers(query))"
          clearable
          filterable
          placeholder="选择员工"
          remote
          width="500px"
        >
          <el-option v-for="item in traderUsers" :key="item.code" :label="item.nickName" :value="item.code">
            <el-image v-if="item.avatarUrl" :src="item.avatarUrl" style="float: left; width: 30px" />
            <span style="float: right">{{ item.nickName }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="储值日期">
        <el-date-picker
          v-model="pageRequest.rechargeDate"
          class="page-form-block-time-range"
          end-placeholder="截止日期"
          start-placeholder="开始日期"
          type="daterange"
        />
      </el-form-item>
      <el-form-item class="page-form-block-search-block">
        <el-button type="primary" @click="loadCustomerRechargeCard">查询</el-button>
        <el-button type="primary" @click="clearAndLoadCustomerRechargeCard">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <el-button @click="showAddCustomerRechargeCardDialog">新建储值单</el-button>
  </div>
  <div>
    <div>
      <el-table :data="customerRechargeCardPageData.data" border style="width: 100%; margin-top: 10px">
        <el-table-column label="会员储值单号" prop="code" width="180" />
        <el-table-column label="会员姓名" prop="customerName" width="120" />
        <el-table-column label="储值卡项" prop="cardTypeName" width="110" />
        <el-table-column label="储值金额" prop="amount" width="110" />
        <el-table-column label="剩余储值金额" prop="balanceAmount" width="125" />
        <el-table-column label="已消耗金额" prop="consumedAmount" width="110" />
        <el-table-column label="已抵扣金额" prop="consumedOriginalAmount" width="110" />
        <el-table-column align="center" label="折扣百分比" prop="discountPercent" width="100" />
        <el-table-column align="center" label="状态" prop="statusName" width="90" />
        <el-table-column align="center" label="储值日期" prop="rechargeDate" width="115" />
        <el-table-column label="成交员工" prop="traderUserName" width="120" />
        <el-table-column align="center" label="过期时间" width="115">
          <template v-slot="{ row }">
            <el-text>{{ expireDate(row) }}</el-text>
          </template>
        </el-table-column>
        <el-table-column label="退卡金额" prop="chargebackAmount" width="110" />
        <el-table-column label="退卡员工" prop="chargebackUserName" width="120" />
        <el-table-column align="center" label="操作">
          <template v-slot="{ row }">
            <el-button v-if="row.status === 'UNPAID'" link type="primary" @click="markPaid(row)"> 收款</el-button>
            <el-button
              v-if="row.status === 'PAID' && row.balanceAmount > 0"
              link
              type="warning"
              @click="chargeback(row)"
            >
              退卡
            </el-button>
            <el-button v-if="row.status === 'WAIT_REFUND'" link type="primary" @click="markRefunded(row)">
              标记退款
            </el-button>
            <el-button link type="primary" @click="showLog(row)"> 日志记录</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination-block">
      <el-pagination
        v-model:current-page="pageRequest.pageIndex"
        v-model:page-size="pageRequest.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="customerRechargeCardPageData.totalCount"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadCustomerRechargeCard"
        @current-change="loadCustomerRechargeCard"
      />
    </div>
  </div>
  <el-dialog v-model="logTableVisible" :title="'日志记录 - ' + currentLogRow?.code" width="1400">
    <div>
      <el-table :data="customerRechargeCardLogData.data">
        <el-table-column label="操作类型" prop="logTypeName" />
        <el-table-column label="金额" prop="amount" />
        <el-table-column label="关联业务单号" prop="sourceBusinessCode" />
        <el-table-column label="操作人" prop="operator" />
        <el-table-column label="操作时间" prop="createTime" />
        <el-table-column label="备注" prop="remark" />
      </el-table>
    </div>
    <div class="pagination-block">
      <el-pagination
        v-model:current-page="logPageInfo.pageIndex"
        v-model:page-size="logPageInfo.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="customerRechargeCardLogData.totalCount"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadCustomerRechargeCard"
        @current-change="loadCustomerRechargeCard"
      />
    </div>
  </el-dialog>
  <el-dialog v-model="addCustomerRechargeCardVisible" title="新建储值单" width="600">
    <el-form :model="addCustomerRechargeCardData" label-width="15%">
      <el-form-item label="会员">
        <el-select
          v-model="addCustomerRechargeCardData.customerCode"
          :remote-method="async (query: any) => (addRechargeCardCustomers = await loadSimpleCustomers(query))"
          filterable
          placeholder="选择会员"
          remote
          style="width: 95%"
        >
          <el-option v-for="item in addRechargeCardCustomers" :key="item.code" :label="item.name" :value="item.code">
            <span style="float: left">{{ item.name }}</span>
            <span class="family-leader-select">{{ item.phone }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="储值卡项">
        <el-select
          v-model="addCustomerRechargeCardData.cardTypeCode"
          clearable
          filterable
          placeholder="选择储值卡项"
          reserve-keyword
          style="width: 95%"
        >
          <el-option v-for="item in rechargeCardTypeOptions" :key="item.code" :label="item.name" :value="item.code" />
        </el-select>
      </el-form-item>
      <el-form-item label="储值日期">
        <el-date-picker
          v-model="addCustomerRechargeCardData.rechargeDate"
          placeholder="储值日期"
          style="width: 95%"
          type="date"
        />
      </el-form-item>
      <el-form-item label="充值金额">
        <el-input-number
          v-model="addCustomerRechargeCardData.amount"
          :controls="false"
          :max="1000000"
          :min="1"
          :precision="2"
          :step="0.01"
          placeholder="充值金额"
          style="width: 95%"
        />
      </el-form-item>
      <el-form-item label="成交员工">
        <el-select
          v-model="addCustomerRechargeCardData.traderUserCode"
          :remote-method="async (query: any) => (addRechargeCardTraderUsers = await loadSimpleUsers(query))"
          clearable
          filterable
          placeholder="选择员工"
          remote
          style="width: 95%"
        >
          <el-option
            v-for="item in addRechargeCardTraderUsers"
            :key="item.code"
            :label="item.nickName"
            :value="item.code"
          >
            <el-image v-if="item.avatarUrl" :src="item.avatarUrl" style="float: left; width: 30px" />
            <span style="float: right">{{ item.nickName }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="备注">
        <el-input
          v-model="addCustomerRechargeCardData.remark"
          autocomplete="off"
          placeholder="备注"
          style="width: 95%"
          type="textarea"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeAddCustomerRechargeCardDialog">取消</el-button>
        <el-button type="primary" @click="confirmAddCustomerRechargeCard"> 确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, toRaw } from "vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { dateFormat } from "@/utils/DateUtil.js";
import { loadSimpleCustomers, SimpleCustomer } from "@/views/customer/customer-list";
import { loadSimpleUsers, SimpleUser } from "@/views/user/user-list";
import { DictOption, loadDictOptions } from "@/views/dict/dict-item";

interface CustomerRechargeCard {
  code: string;
  status: string;
  statusName: string;
  customerCode: string;
  customerName: string;
  cardTypeCode: string;
  cardTypeName: string;
  amount: number;
  balanceAmount: number;
  consumedAmount: number;
  consumedOriginalAmount: number;
  traderUserCode: string;
  traderUserName: string;
  discountPercent: number;
  neverExpire: boolean;
  expireDate: string;
  rechargeDate: string;
  chargebackAmount: number;
  chargebackUserCode: string;
  chargebackUserName: string;
}

const customerRechargeCardPageData = ref({
  totalCount: 0,
  data: [] as CustomerRechargeCard[],
});

const initPageRequest = {
  code: "",
  customerCode: "",
  traderUserCode: "",
  statuses: [] as string[],
  rechargeCardTypes: [],
  rechargeDate: [] as Date[],
  rechargeStartDate: "",
  rechargeEndDate: "",
  pageIndex: 1,
  pageSize: 10,
};

const pageRequest = reactive({ ...initPageRequest });

const addCustomerRechargeCardVisible = ref(false);
const initAddCustomerRechargeCardData = {
  customerCode: "",
  traderUserCode: "",
  cardTypeCode: "",
  rechargeDate: null as Date | null,
  amount: null as number | null,
  remark: "",
};
const addCustomerRechargeCardData = reactive({
  ...initAddCustomerRechargeCardData,
});

function showAddCustomerRechargeCardDialog() {
  Object.assign(addCustomerRechargeCardData, initAddCustomerRechargeCardData);
  addCustomerRechargeCardVisible.value = true;
}

function closeAddCustomerRechargeCardDialog() {
  addCustomerRechargeCardVisible.value = false;
}

function confirmAddCustomerRechargeCard() {
  axios.post("kharazim-api/recharge-card/recharge", toRaw(addCustomerRechargeCardData)).then(() => {
    ElMessage({
      showClose: true,
      message: "创建储值单成功",
      type: "success",
    });
    closeAddCustomerRechargeCardDialog();
    loadCustomerRechargeCard();
  });
}

function clearAndLoadCustomerRechargeCard() {
  Object.assign(pageRequest, initPageRequest);
  loadCustomerRechargeCard();
}

function loadCustomerRechargeCard() {
  const pageParams = toRaw(pageRequest);
  if (pageRequest.rechargeDate && pageRequest.rechargeDate.length === 2) {
    pageParams.rechargeStartDate = dateFormat(pageRequest.rechargeDate[0]);
    pageParams.rechargeEndDate = dateFormat(pageRequest.rechargeDate[1]);
  } else {
    pageParams.rechargeStartDate = "";
    pageParams.rechargeEndDate = "";
  }
  axios
    .get("/kharazim-api/recharge-card/page", { params: pageParams })
    .then((response: AxiosResponse) => (customerRechargeCardPageData.value = response.data));
}

function expireDate(row: CustomerRechargeCard) {
  return row.neverExpire ? "永不过期" : row.expireDate;
}

function markPaid(row: CustomerRechargeCard) {
  ElMessageBox.confirm("确认已收款？", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      axios.put(`/kharazim-api/recharge-card/mark-paid/${row.code}`).then(() => {
        ElMessage({
          type: "success",
          message: "收款成功",
        });
        loadCustomerRechargeCard();
      });
    })
    .catch(() => {});
}

function markRefunded(row: CustomerRechargeCard) {
  ElMessageBox.confirm("确认已退款？", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      axios.put(`/kharazim-api/recharge-card/mark-refunded/${row.code}`).then(() => {
        ElMessage({
          type: "success",
          message: "退款成功",
        });
        loadCustomerRechargeCard();
      });
    })
    .catch(() => {});
}

function chargeback(row: CustomerRechargeCard) {
  ElMessageBox.prompt("请输入退卡金额，最大可退金额: " + row.balanceAmount, "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    inputValue: row.balanceAmount,
  })
    .then(({ value }) => {
      axios
        .post(`/kharazim-api/recharge-card/chargeback`, {
          rechargeCardCode: row.code,
          chargebackAmount: value,
        })
        .then(() => {
          ElMessage({
            type: "success",
            message: "操作成功",
          });
          loadCustomerRechargeCard();
        });
    })
    .catch(() => {});
}

const logTableVisible = ref(false);
const currentLogRow = ref<CustomerRechargeCard>();
const logPageInfo = reactive({
  pageIndex: 1,
  pageSize: 10,
});
const customerRechargeCardLogData = ref({ totalCount: 0, data: [] });

function showLog(row: CustomerRechargeCard) {
  currentLogRow.value = row;
  customerRechargeCardLogData.value = { totalCount: 0, data: [] };
  logPageInfo.pageIndex = 1;
  logPageInfo.pageSize = 10;
  logTableVisible.value = true;
  loadCustomerRechargeCardLog();
}

function loadCustomerRechargeCardLog() {
  if (currentLogRow.value) {
    axios
      .get(`/kharazim-api/recharge-card/page-log/${currentLogRow.value?.code}`, { params: toRaw(logPageInfo) })
      .then((response: AxiosResponse) => {
        customerRechargeCardLogData.value = response.data;
      });
  }
}

const customers = ref<SimpleCustomer[]>([]);
const addRechargeCardCustomers = ref<SimpleCustomer[]>([]);

const traderUsers = ref<SimpleUser[]>([]);
const addRechargeCardTraderUsers = ref<SimpleUser[]>([]);

interface RechargeCardType {
  code: string;
  name: string;
  discountPercent: number;
  neverExpire: boolean;
  validPeriodDays: number;
  canCreateNewCard: boolean;
}

const rechargeCardTypeOptions = ref<RechargeCardType[]>([]);

function loadRechargeCardTypes() {
  axios.get("/kharazim-api/recharge-card-type/list").then((res: AxiosResponse) => {
    rechargeCardTypeOptions.value = res.data.data;
  });
}

const statusOptions = ref<DictOption[]>([]);

onMounted(() => {
  loadDictOptions("customer_recharge_status").then((res: DictOption[]) => (statusOptions.value = res));
  loadCustomerRechargeCard();
  loadRechargeCardTypes();
});
</script>

<style scoped>
:deep(.el-input__inner) {
  text-align: left;
}
</style>
