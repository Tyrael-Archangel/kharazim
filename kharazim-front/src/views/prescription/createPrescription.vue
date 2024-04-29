<template>
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
  <el-form-item label="诊所">
    <el-select
      v-model="createPrescriptionRequest.clinicCode"
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
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";

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

onMounted(() => loadClinicOptions());
</script>

<style scoped>
.customer-select {
  float: right;
  color: var(--el-text-color-secondary);
  font-size: 13px;
}
</style>
