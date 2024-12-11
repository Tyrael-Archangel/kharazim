<template>
  <h1>字典管理</h1>
  <div>
    <el-form
      :inline="true"
      :model="pageRequest"
      class="page-form-block"
      @keyup.enter="loadPrescriptions"
    >
      <el-form-item label="处方编码">
        <el-input
          v-model="pageRequest.prescriptionCode"
          clearable
          placeholder="处方编码"
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
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="pageRequest.createDate"
          end-placeholder="截止时间"
          start-placeholder="开始时间"
          style="width: 280px"
          type="daterange"
        />
      </el-form-item>
      <el-form-item class="page-form-block-search-block">
        <el-button type="primary" @click="loadPrescriptions">查询</el-button>
        <el-button type="primary" @click="resetAndReloadPrescriptions">
          重置
        </el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <div>
      <el-table
        :data="prescriptionPageData.data"
        border
        style="width: 100%; margin-top: 10px"
      >
        <el-table-column type="expand">
          <template v-slot="{ row }">
            <div style="padding: 10px 50px 10px 50px">
              <el-table :data="row.products" border>
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
        <el-table-column label="处方编码" prop="code" width="180" />
        <el-table-column label="会员姓名" prop="customerName" width="150" />
        <el-table-column label="诊所" prop="clinicName" width="160" />
        <el-table-column label="总金额（元）" prop="totalAmount" width="160" />
        <el-table-column label="创建人" prop="creator" width="160" />
        <el-table-column label="创建时间" prop="createTime" width="180" />
        <el-table-column label="备注" prop="remark" />
      </el-table>
    </div>
    <div class="pagination-block">
      <el-pagination
        v-model:current-page="pageRequest.pageIndex"
        v-model:page-size="pageRequest.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="prescriptionPageData.totalCount"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadPrescriptions"
        @current-change="loadPrescriptions"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>

import {reactive, ref} from "vue";
import axios from "@/utils/http.js";
import {AxiosResponse} from "axios";

const dictPageData = ref({ totalCount: 0, data: [] });

const initPageRequest = {
  keywords: "",
  pageIndex: 1,
  pageSize: 10,
};

const pageRequest = reactive({ ...initPageRequest });

function loadPrescriptions() {
  axios
    .get("/kharazim-api//system/dict/types/pages" + formatPageUrlQuery())
    .then((response: AxiosResponse) => {
      prescriptionPageData.value = response.data;
    });
}

</script>

<style scoped></style>
