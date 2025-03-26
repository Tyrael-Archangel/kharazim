<template>
  <div>
    <el-form ref="pageRequestFormRef" :inline="true" :model="pageRequest" class="page-form-block">
      <el-form-item label="会员编码" prop="code">
        <el-input v-model="pageRequest.code" clearable placeholder="会员编码" />
      </el-form-item>
      <el-form-item label="会员名" prop="name">
        <el-input v-model="pageRequest.name" clearable placeholder="会员名" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadCustomer">查询</el-button>
        <el-button type="primary" @click="resetAndLoadCustomer">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <el-link
      :href="`/kharazim-api/customer/export?code=${pageRequest.code}&name=${pageRequest.name}&${ACCESS_TOKEN}=${getToken()}`"
      :underline="false"
    >
      <el-button plain>导出</el-button>
    </el-link>
    <el-table :data="customerPageData" border style="width: 100%; margin-top: 10px">
      <el-table-column label="会员编码" prop="code" width="140">
        <template v-slot="{ row }">
          <el-link type="primary" @click="showDetail(row)">{{ row.code }}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="会员名" prop="name" width="200" />
      <el-table-column align="center" label="性别" width="65">
        <template v-slot="{ row }">
          <el-icon :color="'MALE' === row.gender ? 'blue' : 'red'" size="22">
            <component :is="'MALE' === row.gender ? 'Male' : 'Female'" />
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column label="来源渠道" prop="sourceChannel" width="120" />
      <el-table-column label="创建时间" prop="createTime" width="170" />
      <el-table-column label="备注" prop="remark" />
      <el-table-column align="center" label="操作" width="160">
        <template v-slot="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-link
            style="font-size: small; margin-left: 10px"
            type="primary"
            @click="router.push(`/customer-communication-log?customerCode=${row.code}`)"
          >
            沟通记录
          </el-link>
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
      @size-change="loadCustomer"
      @current-change="loadCustomer"
    />
  </div>

  <el-drawer v-model="detailCustomerVisible" direction="rtl" size="65%" title="会员详情">
    <el-descriptions :column="4" border label-width="5%">
      <el-descriptions-item label="会员名" width="10%">
        {{ detailCustomerData.name }}
      </el-descriptions-item>
      <el-descriptions-item label="会员编码" width="10%">
        {{ detailCustomerData.code }}
      </el-descriptions-item>
      <el-descriptions-item label="性别" width="10%">
        {{ detailCustomerData.genderName }}
      </el-descriptions-item>
      <el-descriptions-item label="年龄" width="10%">
        {{ detailCustomerData.age }}
      </el-descriptions-item>
      <el-descriptions-item label="备注">
        {{ detailCustomerData.remark }}
      </el-descriptions-item>
    </el-descriptions>
    <br />
    <div class="customer-tag-block">
      <div v-for="tag in detailCustomerTags" class="customer-tag">
        <el-tag closable effect="dark" @close="removeTag(tag)">
          {{ tag.tagName }}
        </el-tag>
      </div>
      <div class="customer-tag">
        <el-button size="small" @click="showAddCustomerTag"> 添加标签</el-button>
      </div>
    </div>
    <div class="customer-filed-block">
      <el-text class="customer-block-header">地址信息</el-text>
      <div>
        <el-table :data="customerAddressData" border>
          <el-table-column label="联系人" prop="contact" width="160" />
          <el-table-column label="联系电话" prop="contactPhone" width="140" />
          <el-table-column label="省市区" prop="contactPhone" width="260">
            <template v-slot="{ row }">{{ concatAddress(row) }}</template>
          </el-table-column>
          <el-table-column label="详细地址" prop="detailAddress" />
        </el-table>
      </div>
    </div>
    <div class="customer-filed-block">
      <el-text class="customer-block-header">会员保险</el-text>
      <div>
        <el-table :data="customerInsuranceData" border>
          <el-table-column label="保险公司" prop="companyName" width="160" />
          <el-table-column label="保单号" prop="policyNumber" width="220" />
          <el-table-column label="保险有效期限" prop="duration" width="120" />
          <el-table-column label="保险福利" prop="benefits" />
        </el-table>
      </div>
    </div>
    <div class="customer-filed-block">
      <div class="customer-filed-block-items">
        <div>
          <el-text class="customer-block-header">会员专属客服</el-text>
          <div class="customer-service-user">
            <div class="customer-service-user-avatar">
              <el-avatar :src="fileUrl(customerServiceUserData.serviceUserAvatar)" />
            </div>
            <el-text>{{ customerServiceUserData.serviceUserName }}</el-text>
          </div>
        </div>
        <div>
          <el-text class="customer-block-header">会员专属销售顾问</el-text>
          <div class="customer-service-user">
            <div class="customer-service-user-avatar">
              <el-avatar :src="fileUrl(customerConsultant.salesConsultantAvatar)" />
            </div>
            <el-text>{{ customerConsultant.salesConsultantName }}</el-text>
          </div>
        </div>
        <div />
      </div>
    </div>
    <div class="customer-filed-block">
      <div>
        <el-text class="customer-block-header">会员账户总览</el-text>
        <div style="padding-left: 10px">
          <el-row>
            <el-col :span="2">
              <el-text>储值卡余额</el-text>
            </el-col>
            <el-col :span="4">
              <el-text>{{ customerBalance.totalBalanceAmount }}</el-text>
            </el-col>
            <el-col :span="2">
              <el-text>累计充值金额</el-text>
            </el-col>
            <el-col :span="4">
              <el-text>{{ customerBalance.accumulatedRechargeAmount }}</el-text>
            </el-col>
            <el-col :span="2">
              <el-text>累计消费金额</el-text>
            </el-col>
            <el-col :span="3">
              <el-text>{{ customerBalance.accumulatedConsumedAmount }}</el-text>
            </el-col>
            <el-col :span="3">
              <el-text>最近即将过期金额</el-text>
            </el-col>
            <el-col :span="3">
              <el-text>{{ customerBalance.latestExpireAmount }}</el-text>
            </el-col>
          </el-row>
        </div>
        <div id="customerCardTypeBalancesChart" style="width: 1100px; height: 300px; padding-left: 50px"></div>
      </div>
    </div>
  </el-drawer>

  <el-dialog v-model="editCustomerVisible" title="编辑会员" width="30%">
    <el-form :model="editCustomerData">
      <el-form-item label="会员编码">
        <el-text>{{ editCustomerData.code }}</el-text>
      </el-form-item>
      <el-form-item label="会员名">
        <el-input v-model="editCustomerData.name" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="editCustomerVisible = false">取消</el-button>
        <el-button type="primary" @click="saveCustomerEdit"> 保存</el-button>
      </div>
    </template>
  </el-dialog>
  <el-dialog v-model="addCustomerTagVisible" title="选择标签" width="30%">
    <div class="customer-tag-block">
      <div v-for="tagOption in allDictOptions" class="customer-tag">
        <el-button
          v-if="tagOption.canSelect"
          :type="tagOption.selected ? 'primary' : ''"
          size="small"
          @click="tagOption.selected = !tagOption.selected"
        >
          {{ tagOption.value }}
        </el-button>
        <el-button v-else disabled size="small" type="primary">
          {{ tagOption.value }}
        </el-button>
      </div>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="confirmAddTags">确认</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { nextTick, onMounted, reactive, ref } from "vue";
import axios from "@/utils/http.js";
import { ACCESS_TOKEN, getToken } from "@/utils/auth.js";
import { AxiosResponse } from "axios";
import * as echarts from "echarts/core";
import { FormInstance } from "element-plus";
import { DictOption, loadDictOptions } from "@/views/dict/dict-item";
import { useRouter } from "vue-router";
import { fileUrl } from "@/utils/fileUrl.ts";

const router = useRouter();

interface CustomerData {
  code: string;
  name: string;
  gender: string;
  genderName: string;
  birthYear: number | null;
  birthMonth: number | null;
  birthDayOfMonth: number | null;
  age: number | null;
  remark: string;
  sourceChannel: string;
  createTime: string;
}

const customerPageData = ref<CustomerData[]>([]);

const pageRequestFormRef = ref<FormInstance>();
const pageRequest = ref({
  code: "",
  name: "",
});

const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [10, 20, 50, 100],
});

function loadCustomer() {
  axios
    .get("/kharazim-api/customer/page", {
      params: {
        pageSize: pageInfo.pageSize,
        pageIndex: pageInfo.currentPage,
        code: pageRequest.value.code,
        name: pageRequest.value.name,
      },
    })
    .then((response: AxiosResponse) => {
      customerPageData.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
    });
}

function resetAndLoadCustomer() {
  pageRequestFormRef.value?.resetFields();
  loadCustomer();
}

const detailCustomerData = ref<CustomerData>({
  code: "",
  name: "",
  gender: "",
  genderName: "",
  birthYear: null,
  birthMonth: null,
  birthDayOfMonth: null,
  age: null,
  remark: "",
  sourceChannel: "",
  createTime: "",
});

interface CustomerTag {
  tagName: "";
  tagDictKey: "";
}

const detailCustomerTags = ref<CustomerTag[]>([]);

const detailCustomerVisible = ref(false);

const showDetail = (customerData: CustomerData) => {
  Object.assign(detailCustomerData.value, customerData);
  detailCustomerTags.value = [];
  customerAddressData.value = [];
  customerInsuranceData.value = [];
  detailCustomerVisible.value = !detailCustomerVisible.value;
  queryCustomerTags(customerData);
  queryCustomerAddress(customerData);
  queryInsuranceAddress(customerData);
  queryCustomerServiceUserData(customerData);
  queryCustomerSalesConsultantData(customerData);
  loadCustomerRechargeCardBalance(customerData);
  loadCustomerCardTypeBalances(customerData);
};

const queryCustomerTags = (customerData: CustomerData) => {
  axios.get(`/kharazim-api/customer/tags/${customerData.code}`).then((response: AxiosResponse) => {
    detailCustomerTags.value = response.data.data;
  });
};

function removeTag(tag: CustomerTag) {
  const customerData = detailCustomerData.value;
  axios
    .delete(`/kharazim-api/customer/tags/${customerData.code}/${tag.tagDictKey}`)
    .then(() => queryCustomerTags(customerData));
}

const addCustomerTagVisible = ref(false);

interface CanChoiceTagOption {
  key: string;
  value: string;
  selected: boolean;
  canSelect: boolean;
}

const allDictOptions = ref<CanChoiceTagOption[]>([]);

function showAddCustomerTag() {
  allDictOptions.value = [] as CanChoiceTagOption[];
  loadDictOptions("customer_tag").then((dictOptions: DictOption[]) => {
    const existTagKeys = detailCustomerTags.value.map((customerTag: CustomerTag) => customerTag.tagDictKey) as string[];

    allDictOptions.value = dictOptions.map((dictOption: DictOption) => {
      return {
        key: dictOption.key,
        value: dictOption.value,
        selected: false,
        canSelect: !existTagKeys.includes(dictOption.key),
      } as CanChoiceTagOption;
    });
    addCustomerTagVisible.value = true;
  });
}

function confirmAddTags() {
  const addTagKeys = allDictOptions.value
    .filter((tagOption) => tagOption.canSelect && tagOption.selected)
    .map((tagOption) => tagOption.key);
  const customerData = detailCustomerData.value;
  if (addTagKeys.length > 0) {
    axios
      .post("/kharazim-api/customer/tags", {
        customerCode: customerData.code,
        tagDictKeys: addTagKeys,
      })
      .then(() => {
        queryCustomerTags(customerData);
      });
  }
  addCustomerTagVisible.value = false;
}

const editCustomerData = ref<CustomerData>({
  code: "",
  name: "",
  gender: "",
  genderName: "",
  birthYear: null,
  birthMonth: null,
  birthDayOfMonth: null,
  age: null,
  remark: "",
  sourceChannel: "",
  createTime: "",
});

const editCustomerVisible = ref(false);

const handleEdit = (row: CustomerData) => {
  editCustomerVisible.value = !editCustomerVisible.value;
  Object.assign(editCustomerData.value, row);
};

const saveCustomerEdit = () => {
  editCustomerVisible.value = !editCustomerVisible.value;
};

interface CustomerAddress {
  customerAddressId: number;
  contact: string;
  contactPhone: string;
  provinceCode: string;
  provinceName: string;
  cityCode: string;
  cityName: string;
  countyCode: string;
  countyName: string;
  detailAddress: string;
  defaultAddress: boolean;
}

const customerAddressData = ref<CustomerAddress[]>([]);
const queryCustomerAddress = (customerData: CustomerData) => {
  axios.get(`/kharazim-api/customer/address/${customerData.code}`).then((response: AxiosResponse) => {
    customerAddressData.value = response.data.data;
  });
};

function concatAddress(address: CustomerAddress) {
  return address.provinceName + address.cityName + address.countyName;
}

interface CustomerInsurance {
  customerInsuranceId: number;
  companyName: string;
  companyDictKey: string;
  policyNumber: string;
  duration: string;
  benefits: string;
  defaultInsurance: boolean;
}

const customerInsuranceData = ref<CustomerInsurance[]>([]);
const queryInsuranceAddress = (customerData: CustomerData) => {
  axios.get(`/kharazim-api/customer/insurance/${customerData.code}`).then((response: AxiosResponse) => {
    customerInsuranceData.value = response.data.data;
  });
};

interface CustomerServiceUser {
  serviceUserCode: string;
  serviceUserName: string;
  serviceUserAvatar: string;
  updateTime: string;
}

const customerServiceUserData = ref<CustomerServiceUser>({
  serviceUserCode: "",
  serviceUserName: "",
  serviceUserAvatar: "",
  updateTime: "",
});

const queryCustomerServiceUserData = (customerData: CustomerData) => {
  axios.get(`/kharazim-api/customer/service/${customerData.code}`).then((response: AxiosResponse) => {
    customerServiceUserData.value = response.data.data;
  });
};

interface CustomerSalesConsultant {
  salesConsultantCode: string;
  salesConsultantName: string;
  salesConsultantAvatar: string;
  updateTime: string;
}

const customerConsultant = ref<CustomerSalesConsultant>({
  salesConsultantCode: "",
  salesConsultantName: "",
  salesConsultantAvatar: "",
  updateTime: "",
});

const queryCustomerSalesConsultantData = (customerData: CustomerData) => {
  axios.get(`/kharazim-api/customer/sales-consultant/${customerData.code}`).then((response: AxiosResponse) => {
    customerConsultant.value = response.data.data;
  });
};

interface CustomerBalanceOverview {
  customerCode: string;
  customerName: string;
  totalBalanceAmount: number;
  accumulatedRechargeAmount: number;
  accumulatedConsumedAmount: number;
  latestExpireAmount: number;
  expireDate: string;
}

const customerBalance = ref<CustomerBalanceOverview>({
  customerCode: "",
  customerName: "",
  totalBalanceAmount: 0,
  accumulatedRechargeAmount: 0,
  accumulatedConsumedAmount: 0,
  latestExpireAmount: 0,
  expireDate: "",
});

function loadCustomerRechargeCardBalance(customerData: CustomerData) {
  axios
    .get(`/kharazim-api/recharge-card/customer/balance-overview/${customerData.code}`)
    .then((response: AxiosResponse) => {
      customerBalance.value = response.data.data;
    });
}

interface CustomerCardTypeBalance {
  customerCode: string;
  customerName: string;
  balanceAmount: number;
  cardTypeCode: string;
  cardTypeName: string;
}

const customerCardTypeBalances = ref<CustomerCardTypeBalance[]>([]);

function loadCustomerCardTypeBalances(customerData: CustomerData) {
  axios
    .get(`/kharazim-api/recharge-card/customer/card-type-balance/${customerData.code}`)
    .then((response: AxiosResponse) => {
      customerCardTypeBalances.value = response.data.data;
      renderChart();
    });
}

function renderChart() {
  let cardTypes = customerCardTypeBalances.value.map((x) => x.cardTypeName);
  let balanceAmounts = customerCardTypeBalances.value.map((x) => x.balanceAmount);
  nextTick(() => {
    let cardTypeBalancesChart = echarts.init(document.getElementById("customerCardTypeBalancesChart"));
    const option = {
      tooltip: {},
      legend: {
        data: ["剩余金额"],
      },
      xAxis: {
        data: cardTypes,
      },
      yAxis: {},
      series: [
        {
          name: "剩余金额",
          type: "bar",
          data: balanceAmounts,
        },
      ],
    };
    cardTypeBalancesChart.setOption(option);
  });
}

onMounted(() => loadCustomer());
</script>

<style scoped>
.customer-tag-block {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  padding-bottom: 15px;
  width: 100%;
}

.customer-tag {
  padding: 3px;
}

.customer-filed-block {
  margin-top: 10px;
  padding: 10px;
  border: #dcdfe6 1px solid;
}

.customer-filed-block-items {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.customer-block-header {
  color: darkorange;
  padding: 8px;
  display: block;
}

.customer-service-user {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  width: 100%;
}

.customer-service-user-avatar {
  padding: 15px;
}
</style>
