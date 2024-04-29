<template>
  <div>
    <el-form
      :inline="true"
      :model="pageRequest"
      class="page-form-block"
      @keyup.enter="loadPrescriptions"
    >
      <el-form-item label="会员">
        <el-select
          v-model="pageRequest.customerCode"
          :remote-method="loadCustomers"
          filterable
          placeholder="选择会员"
          remote
          width="500px"
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
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="pageRequest.createDate"
          end-placeholder="截止时间"
          start-placeholder="开始时间"
          type="daterange"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadPrescriptions">查询</el-button>
        <el-button type="primary" @click="resetRequestAndLoadPrescriptions"
          >重置
        </el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <router-link to="/create-prescription">
      <el-button type="primary">创建处方</el-button>
    </router-link>
  </div>
  <div>
    <div>
      <el-table
        :data="prescriptionPageData"
        border
        style="width: 100%; margin-top: 10px"
      >
        <el-table-column label="处方编码" prop="code" />
        <el-table-column label="会员姓名" prop="customerName" />
        <el-table-column label="诊所" prop="clinicName" />
        <el-table-column label="总金额（元）" prop="totalAmount" />
        <el-table-column label="备注" prop="remark" />
        <el-table-column label="创建人" prop="creator" />
        <el-table-column label="创建时间" prop="createTime" />
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
        @size-change="loadPrescriptions"
        @current-change="loadPrescriptions"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";
import { dateFormat } from "@/utils/DateUtil.js";

interface Prescription {
  code: string;
  name: string;
  englishName: string;
}

const prescriptionPageData = ref<Prescription[]>([]);
const pageRequest = reactive({
  customerCode: "",
  clinicCodes: [],
  createDateMin: "",
  createDateMax: "",
  createDate: [] as Date[],
});
const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [10, 20, 50, 100],
});

function resetRequestAndLoadPrescriptions() {
  pageRequest.customerCode = "";
  pageRequest.clinicCodes = [];
  pageRequest.createDateMin = "";
  pageRequest.createDateMax = "";
  pageRequest.createDate = [] as Date[];
  loadPrescriptions();
}

function loadPrescriptions() {
  if (pageRequest.createDate && pageRequest.createDate.length === 2) {
    pageRequest.createDateMin = dateFormat(pageRequest.createDate[0]);
    pageRequest.createDateMax = dateFormat(pageRequest.createDate[1]);
  } else {
    pageRequest.createDateMin = "";
    pageRequest.createDateMax = "";
  }
  console.log(pageRequest);
  axios
    .get("/kharazim-api/prescription/page", {
      params: {
        customerCode: pageRequest.customerCode,
        clinicCodes: pageRequest.clinicCodes,
        createDateMin: pageRequest.createDateMin,
        createDateMax: pageRequest.createDateMax,
        pageSize: pageInfo.pageSize,
        pageNum: pageInfo.currentPage,
      },
    })
    .then((response: AxiosResponse) => {
      prescriptionPageData.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
    });
}

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

onMounted(() => {
  loadPrescriptions();
  loadClinicOptions();
});
</script>

<style scoped>
.customer-select {
  float: right;
  color: var(--el-text-color-secondary);
  font-size: 13px;
}
</style>
