<template>
  <div>
    <el-form
      :inline="true"
      :model="pageRequest"
      class="page-form-block"
      @keyup.enter="loadInventories"
    >
      <el-form-item label="商品编码">
        <el-input
          v-model="pageRequest.skuCode"
          clearable
          placeholder="商品编码"
        />
      </el-form-item>
      <el-form-item label="商品名称">
        <el-input
          v-model="pageRequest.skuName"
          clearable
          placeholder="商品名称"
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
      <el-form-item class="page-form-block-search-block">
        <el-button type="primary" @click="loadInventories">查询</el-button>
        <el-button type="primary" @click="resetAndLoadInventories"
          >重置
        </el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <div>
      <el-table
        :data="inventoryPageData"
        border
        style="width: 100%; margin-top: 10px"
      >
        <el-table-column label="商品编码" prop="skuCode" />
        <el-table-column label="诊所" prop="clinicName" />
        <el-table-column label="商品名称" prop="skuName" />
        <el-table-column align="center" label="在库库存" prop="quantity" />
        <el-table-column
          align="center"
          label="可用库存"
          prop="usableQuantity"
        />
        <el-table-column
          align="center"
          label="预占数量"
          prop="occupiedQuantity"
        />
        <el-table-column align="center" label="预占数量">
          <template v-slot="{ row }">
            <el-button link type="primary" @click="showSkuOccupyRecord(row)">
              {{ row.occupiedQuantity }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="商品主图">
          <template v-slot="{ row }">
            <el-image
              v-if="row.defaultImageUrl"
              :src="row.defaultImageUrl"
              style="width: 40px"
            >
            </el-image>
          </template>
        </el-table-column>
        <el-table-column align="center" label="单位" prop="unitName" />
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
        @size-change="loadInventories"
        @current-change="loadInventories"
      />
    </div>
  </div>
  <el-dialog v-model="skuOccupyRecordVisible" title="商品预占情况" width="50%">
    <el-table
      :data="skuOccupyRecordPageData"
      border
      style="width: 100%; margin-top: 10px"
    >
      <el-table-column label="单据编码" prop="businessCode" />
      <el-table-column label="数量" prop="quantity" />
    </el-table>
    <div class="pagination-block">
      <el-pagination
        v-model:current-page="skuOccupyRecordPageInfo.currentPage"
        v-model:page-size="skuOccupyRecordPageInfo.pageSize"
        :page-sizes="skuOccupyRecordPageInfo.pageSizes"
        :total="skuOccupyRecordPageInfo.totalCount"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadSkuOccupyRecords"
        @current-change="loadSkuOccupyRecords"
      />
    </div>
  </el-dialog>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import axios from "@/utils/http.js";
import { AxiosResponse } from "axios";

const inventoryPageData = ref([]);

const pageRequest = reactive({
  skuCode: "",
  skuName: "",
  clinicCodes: [],
});

const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [10, 20, 50, 100],
});

function loadInventories() {
  axios
    .get("/kharazim-api/inventory/page", {
      params: {
        skuCode: pageRequest.skuCode,
        skuName: pageRequest.skuName,
        clinicCodes: pageRequest.clinicCodes,
        pageSize: pageInfo.pageSize,
        pageNum: pageInfo.currentPage,
      },
    })
    .then((response: AxiosResponse) => {
      inventoryPageData.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
    });
}

function resetAndLoadInventories() {
  pageRequest.skuCode = "";
  pageRequest.skuName = "";
  pageRequest.clinicCodes = [];
  pageInfo.currentPage = 1;
  pageInfo.pageSize = 10;
  pageInfo.totalCount = 0;
  loadInventories();
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

function showSkuOccupyRecord(row: any) {
  currentSkuOccupyRow.value = row;
  skuOccupyRecordPageInfo.currentPage = 1;
  skuOccupyRecordPageInfo.pageSize = 10;
  skuOccupyRecordPageInfo.totalCount = 0;
  loadSkuOccupyRecords();
  skuOccupyRecordVisible.value = true;
}

const skuOccupyRecordVisible = ref(false);
const skuOccupyRecordPageData = ref([]);

const currentSkuOccupyRow = ref();

const skuOccupyRecordPageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [10, 20, 50, 100],
});

function loadSkuOccupyRecords() {
  axios
    .get("/kharazim-api/inventory/page-occupy", {
      params: {
        skuCode: currentSkuOccupyRow.value.skuCode,
        clinicCode: currentSkuOccupyRow.value.clinicCode,
        pageSize: skuOccupyRecordPageInfo.pageSize,
        pageNum: skuOccupyRecordPageInfo.currentPage,
      },
    })
    .then((res: AxiosResponse) => {
      skuOccupyRecordPageData.value = res.data.data;
      skuOccupyRecordPageInfo.totalCount = res.data.totalCount;
    });
}

onMounted(() => {
  loadInventories();
  loadClinicOptions();
});
</script>

<style scoped></style>
