<template>
  <div>
    <el-table :data="customerPageData" border style="width: 100%">
      <el-table-column label="会员编码" prop="code" width="140">
        <template v-slot="{ row }">
          <el-link type="primary" @click="showDetail(row)"
            >{{ row.code }}
          </el-link>
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
      <el-table-column align="center" label="操作" width="100">
        <template v-slot="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
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

  <el-drawer
    v-model="detailCustomerVisible"
    direction="rtl"
    size="65%"
    title="会员详情"
  >
    <h4>{{ detailCustomerData.code }}</h4>
    <div class="customer-tag-block">
      <div v-for="tag in detailCustomerTags" class="customer-tag">
        <el-tag>{{ tag.tagName }}</el-tag>
      </div>
    </div>
    <el-descriptions :column="2" border>
      <el-descriptions-item label="会员名">
        {{ detailCustomerData.name }}
      </el-descriptions-item>
      <el-descriptions-item label="性别">
        {{ detailCustomerData.gender === "MALE" ? "男" : "女" }}
      </el-descriptions-item>
      <el-descriptions-item label="备注">
        {{ detailCustomerData.remark }}
      </el-descriptions-item>
    </el-descriptions>
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
              <el-avatar :src="customerServiceUserData.serviceUserAvatarUrl" />
            </div>
            <el-text>{{ customerServiceUserData.serviceUserName }}</el-text>
          </div>
        </div>
        <div>
          <el-text class="customer-block-header">会员专属销售顾问</el-text>
          <div class="customer-service-user">
            <div class="customer-service-user-avatar">
              <el-avatar :src="customerConsultant.salesConsultantAvatarUrl" />
            </div>
            <el-text>{{ customerConsultant.salesConsultantName }}</el-text>
          </div>
        </div>
        <div />
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
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import axios from "@/utils/http.js";
import { AxiosResponse } from "axios";

interface CustomerData {
  code: string;
  name: string;
  gender: string;
  birthYear: number | null;
  birthMonth: number | null;
  birthDayOfMonth: number | null;
  remark: string;
  sourceChannel: string;
  createTime: string;
}

const customerPageData = ref<CustomerData[]>([]);
const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [10, 20, 50, 100],
});

function loadCustomer() {
  axios
    .get(
      `/kharazim-api/customer/page?pageSize=${pageInfo.pageSize}&pageNum=${pageInfo.currentPage}`,
    )
    .then((response: AxiosResponse) => {
      customerPageData.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
    });
}

const detailCustomerData = ref<CustomerData>({
  code: "",
  name: "",
  gender: "",
  birthYear: null,
  birthMonth: null,
  birthDayOfMonth: null,
  remark: "",
  sourceChannel: "",
  createTime: "",
});
const detailCustomerTags = ref([{ tagName: "", tagDictValue: "" }]);

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
};

const queryCustomerTags = (customerData: CustomerData) => {
  axios
    .get(`/kharazim-api/customer/tags/${customerData.code}`)
    .then((response: AxiosResponse) => {
      detailCustomerTags.value = response.data.data;
    });
};

const editCustomerData = ref<CustomerData>({
  code: "",
  name: "",
  gender: "",
  birthYear: null,
  birthMonth: null,
  birthDayOfMonth: null,
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
  axios
    .get(`/kharazim-api/customer/address/${customerData.code}`)
    .then((response: AxiosResponse) => {
      customerAddressData.value = response.data.data;
    });
};

function concatAddress(address: CustomerAddress) {
  return address.provinceName + address.cityName + address.countyName;
}

interface CustomerInsurance {
  customerInsuranceId: number;
  companyName: string;
  companyDictValue: string;
  policyNumber: string;
  duration: string;
  benefits: string;
  defaultInsurance: boolean;
}

const customerInsuranceData = ref<CustomerInsurance[]>([]);
const queryInsuranceAddress = (customerData: CustomerData) => {
  axios
    .get(`/kharazim-api/customer/insurance/${customerData.code}`)
    .then((response: AxiosResponse) => {
      customerInsuranceData.value = response.data.data;
    });
};

interface CustomerServiceUser {
  serviceUserCode: string;
  serviceUserName: string;
  serviceUserAvatar: string;
  serviceUserAvatarUrl: string;
  updateTime: string;
}

const customerServiceUserData = ref<CustomerServiceUser>({
  serviceUserCode: "",
  serviceUserName: "",
  serviceUserAvatar: "",
  serviceUserAvatarUrl: "",
  updateTime: "",
});

const queryCustomerServiceUserData = (customerData: CustomerData) => {
  axios
    .get(`/kharazim-api/customer/service/${customerData.code}`)
    .then((response: AxiosResponse) => {
      customerServiceUserData.value = response.data.data;
    });
};

interface CustomerSalesConsultant {
  salesConsultantCode: string;
  salesConsultantName: string;
  salesConsultantAvatar: string;
  salesConsultantAvatarUrl: string;
  updateTime: string;
}

const customerConsultant = ref<CustomerSalesConsultant>({
  salesConsultantCode: "",
  salesConsultantName: "",
  salesConsultantAvatar: "",
  salesConsultantAvatarUrl: "",
  updateTime: "",
});

const queryCustomerSalesConsultantData = (customerData: CustomerData) => {
  axios
    .get(`/kharazim-api/customer/sales-consultant/${customerData.code}`)
    .then((response: AxiosResponse) => {
      customerConsultant.value = response.data.data;
    });
};

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