<template>
  <div>
    <el-form
      :inline="true"
      :model="pageRequest"
      class="page-form-block"
      @keyup.enter="loadSettlementOrders"
    >
      <el-form-item label="结算单编码">
        <el-input
          v-model="pageRequest.settlementOrderCode"
          clearable
          placeholder="结算单编码"
        />
      </el-form-item>
      <el-form-item label="会员">
        <el-select
          v-model="pageRequest.customerCode"
          :remote-method="loadCustomers"
          clearable
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
      <el-form-item>
        <el-button type="primary" @click="loadSettlementOrders">查询</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <div>
      <el-table
        :data="settlementOrderPageData"
        border
        style="width: 100%; margin-top: 10px"
      >
        <el-table-column type="expand">
          <template v-slot="{ row }">
            <div style="padding: 10px 50px 10px 50px">
              <el-table :data="row.items" border>
                <el-table-column align="center" type="index" width="50" />
                <el-table-column align="center" label="商品主图" width="160">
                  <template v-slot="{ row: product }">
                    <el-image
                      v-if="product.defaultImageUrl"
                      :src="product.defaultImageUrl"
                      style="width: 50px"
                    >
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
            <el-link
              :href="
                '/#/prescription-info?prescriptionCode=' +
                row.sourcePrescriptionCode
              "
              type="primary"
              >{{ row.sourcePrescriptionCode }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="会员姓名" prop="customerName" />
        <el-table-column label="诊所" prop="clinicName" />
        <el-table-column
          label="总金额（元）"
          align="center"
          prop="totalAmount"
        />
        <el-table-column label="创建时间" prop="createTime" />
        <el-table-column label="结算时间" prop="settlementTime" />
        <el-table-column label="结算状态" align="center">
          <template v-slot="{ row }">
            <el-tag v-if="row.status === 'PAID'" type="success">
              <el-icon>
                <CircleCheck />
              </el-icon>
              已结算
            </el-tag>
            <el-tag v-else type="warning">
              <el-icon>
                <Warning />
              </el-icon>
              待结算
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination-block">
      <el-pagination
        v-model:current-page="pageRequest.pageNum"
        v-model:page-size="pageRequest.pageSize"
        :page-sizes="pageInfo.pageSizes"
        :total="pageInfo.totalCount"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadSettlementOrders"
        @current-change="loadSettlementOrders"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";
import { CircleCheck, Warning } from "@element-plus/icons-vue";

const settlementOrderPageData = ref([]);
const pageInfo = reactive({
  totalCount: 0,
  pageSizes: [10, 20, 50, 100],
});

const pageRequest = reactive({
  settlementOrderCode: "",
  customerCode: "",
  clinicCodes: [],
  status: "",
  pageSize: 10,
  pageNum: 1,
});

function loadSettlementOrders() {
  axios
    .get("/kharazim-api/settlement-order/page", {
      params: pageRequest,
    })
    .then((response: AxiosResponse) => {
      settlementOrderPageData.value = response.data.data;
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
  loadSettlementOrders();
  loadClinicOptions();
});
</script>

<style scoped></style>