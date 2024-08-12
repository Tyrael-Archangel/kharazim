<template>
  <div>
    <el-form
      :inline="true"
      :model="pageRequest"
      class="page-form-block"
      @keyup.enter="loadPurchaseOrders"
    >
      <el-form-item label="采购单编码">
        <el-input
          v-model="pageRequest.purchaseOrderCode"
          clearable
          placeholder="采购单编码"
        />
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
      <el-form-item label="收货状态">
        <el-select
          v-model="pageRequest.receiveStatuses"
          clearable
          filterable
          multiple
          placeholder="选择收货状态"
          reserve-keyword
        >
          <el-option key="WAIT_RECEIVE" label="待收货" value="WAIT_RECEIVE" />
          <el-option key="RECEIVING" label="收货中" value="RECEIVING" />
          <el-option
            key="RECEIVE_FINISHED"
            label="收货完结"
            value="RECEIVE_FINISHED"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="结算状态">
        <el-select
          v-model="pageRequest.paymentStatuses"
          clearable
          filterable
          multiple
          placeholder="选择结算状态"
          reserve-keyword
        >
          <el-option key="UNPAID" label="待结算" value="UNPAID" />
          <el-option key="PART_PAID" label="部分结算" value="PART_PAID" />
          <el-option key="ALL_PAID" label="已结算" value="ALL_PAID" />
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
        <el-button type="primary" @click="loadPurchaseOrders">查询</el-button>
        <el-button type="primary" @click="resetAndReloadPurchaseOrders">
          重置
        </el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <router-link to="/create-purchase-order">
      <el-button type="primary">创建采购单</el-button>
    </router-link>
  </div>
  <div>
    <div>
      <el-table
        :data="purchaseOrderPageData"
        border
        style="width: 100%; margin-top: 10px"
      >
        <el-table-column type="expand">
          <template v-slot="{ row }">
            <div style="padding: 10px 50px 10px 50px">
              <el-table :data="row.items" border>
                <el-table-column align="center" type="index" width="50" />
                <el-table-column align="center" label="商品主图" width="160">
                  <template v-slot="{ row: item }">
                    <el-image
                      v-if="item.defaultImageUrl"
                      :src="item.defaultImageUrl"
                      style="width: 50px"
                    >
                    </el-image>
                  </template>
                </el-table-column>
                <el-table-column label="商品编码" prop="skuCode" />
                <el-table-column label="商品名称" prop="skuName" />
                <el-table-column label="单位" prop="unitName" width="160" />
                <el-table-column label="商品分类" prop="categoryName" />
                <el-table-column align="center" label="单价" prop="price" />
                <el-table-column
                  align="center"
                  label="采购数量"
                  prop="quantity"
                />
                <el-table-column
                  align="center"
                  label="已收货数量"
                  prop="receivedQuantity"
                />
                <el-table-column align="center" label="小计" prop="amount" />
              </el-table>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="采购单编码" prop="code" width="160" />
        <el-table-column label="诊所" prop="clinicName" width="160" />
        <el-table-column label="供应商" prop="supplierName" width="160" />
        <el-table-column
          label="收货状态"
          prop="receiveStatusName"
          width="100"
        />
        <el-table-column
          label="结算状态"
          prop="paymentStatusName"
          width="100"
        />
        <el-table-column label="总金额（元）" prop="totalAmount" width="120" />
        <el-table-column
          label="已结算金额（元）"
          prop="paidAmount"
          width="150"
        />
        <el-table-column label="创建人" prop="creator" width="160" />
        <el-table-column label="创建时间" prop="createTime" width="180" />
        <el-table-column label="备注" min-width="330" prop="remark" />
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
        @size-change="loadPurchaseOrders"
        @current-change="loadPurchaseOrders"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";
import { dateFormat } from "@/utils/DateUtil";

const purchaseOrderPageData = ref([]);

const pageRequest = reactive({
  purchaseOrderCode: "",
  clinicCodes: [],
  supplierCodes: [],
  receiveStatuses: [],
  paymentStatuses: [],
  createDate: [] as Date[],
});
const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [10, 20, 50, 100],
});

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

interface SupplierOption {
  code: string;
  name: string;
}

const supplierOptions = ref<SupplierOption[]>([]);

function loadSupplierOptions() {
  axios.get("/kharazim-api/supplier/list").then((res: AxiosResponse) => {
    supplierOptions.value = res.data.data;
  });
}

function resetAndReloadPurchaseOrders() {
  pageRequest.purchaseOrderCode = "";
  pageRequest.clinicCodes = [];
  pageRequest.supplierCodes = [];
  pageRequest.receiveStatuses = [];
  pageRequest.paymentStatuses = [];
  pageRequest.createDate = [] as Date[];
  loadPurchaseOrders();
}

function loadPurchaseOrders() {
  let createDateMin = "";
  let createDateMax = "";
  if (pageRequest.createDate && pageRequest.createDate.length === 2) {
    createDateMin = dateFormat(pageRequest.createDate[0]);
    createDateMax = dateFormat(pageRequest.createDate[1]);
  }

  axios
    .get("/kharazim-api/purchase-order/page", {
      params: {
        purchaseOrderCode: pageRequest.purchaseOrderCode,
        clinicCodes: pageRequest.clinicCodes,
        supplierCodes: pageRequest.supplierCodes,
        receiveStatuses: pageRequest.receiveStatuses,
        paymentStatuses: pageRequest.paymentStatuses,
        pageSize: pageInfo.pageSize,
        pageNum: pageInfo.currentPage,
        createDateMin: createDateMin,
        createDateMax: createDateMax,
      },
    })
    .then((response: AxiosResponse) => {
      purchaseOrderPageData.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
    });
}

onMounted(() => {
  loadPurchaseOrders();
  loadClinicOptions();
  loadSupplierOptions();
});
</script>

<style scoped></style>
