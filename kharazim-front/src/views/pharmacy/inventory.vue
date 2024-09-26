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
      <el-form-item label="排序方式">
        <el-button
          :type="getSortButtonType('QUANTITY')"
          @click="switchSort('QUANTITY')"
        >
          在库库存
          <el-icon>
            <component :is="getSortDirectionIcon('QUANTITY')" />
          </el-icon>
        </el-button>
        <el-button
          :type="getSortButtonType('USABLE_QUANTITY')"
          @click="switchSort('USABLE_QUANTITY')"
        >
          可用库存
          <el-icon>
            <component :is="getSortDirectionIcon('USABLE_QUANTITY')" />
          </el-icon>
        </el-button>
        <el-button
          :type="getSortButtonType('OCCUPIED_QUANTITY')"
          @click="switchSort('OCCUPIED_QUANTITY')"
        >
          预占库存
          <el-icon>
            <component :is="getSortDirectionIcon('OCCUPIED_QUANTITY')" />
          </el-icon>
        </el-button>
      </el-form-item>
      <el-form-item class="page-form-block-search-block">
        <el-button type="primary" @click="loadInventories">查询</el-button>
        <el-button type="primary" @click="resetAndLoadInventories">
          重置
        </el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <div>
      <el-table
        :data="inventoryPageData.data"
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
        v-model:current-page="pageRequest.pageIndex"
        v-model:page-size="pageRequest.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="inventoryPageData.totalCount"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadInventories"
        @current-change="loadInventories"
      />
    </div>
  </div>
  <el-dialog v-model="skuOccupyRecordVisible" title="商品预占情况" width="30%">
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
import { onMounted, reactive, ref, toRaw } from "vue";
import axios from "@/utils/http.js";
import { AxiosResponse } from "axios";
import {
  ClinicVO,
  loadClinicOptions,
} from "@/views/clinic/clinicManagement.vue";

const inventoryPageData = ref({ totalCount: 0, data: [] });

const initPageRequest = {
  skuCode: "",
  skuName: "",
  clinicCodes: [],
  sortBy: "QUANTITY",
  sortDirection: "DESC",
  pageIndex: 1,
  pageSize: 10,
};

const pageRequest = reactive({ ...initPageRequest });

function switchSort(btn: string) {
  if (btn === pageRequest.sortBy) {
    pageRequest.sortDirection =
      pageRequest.sortDirection === "ASC" ? "DESC" : "ASC";
  } else {
    pageRequest.sortBy = btn;
    pageRequest.sortDirection = "DESC";
  }
  loadInventories();
}

function getSortButtonType(btn: string) {
  if (btn === pageRequest.sortBy) {
    return "primary";
  } else {
    return "";
  }
}

function getSortDirectionIcon(btn: string) {
  if (btn !== pageRequest.sortBy) {
    // 当前按钮非排序字段
    return "DCaret";
  }
  // 当前按钮为排序字段
  return "DESC" === pageRequest.sortDirection ? "CaretBottom" : "CaretTop";
}

function loadInventories() {
  axios
    .get("/kharazim-api/inventory/page", { params: toRaw(pageRequest) })
    .then((response: AxiosResponse) => {
      inventoryPageData.value = response.data;
    });
}

function resetAndLoadInventories() {
  Object.assign(pageRequest, initPageRequest);
  loadInventories();
}

const clinicOptions = ref<ClinicVO[]>([]);

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
        pageIndex: skuOccupyRecordPageInfo.currentPage,
      },
    })
    .then((res: AxiosResponse) => {
      skuOccupyRecordPageData.value = res.data.data;
      skuOccupyRecordPageInfo.totalCount = res.data.totalCount;
    });
}

onMounted(async () => {
  loadInventories();
  clinicOptions.value = await loadClinicOptions();
});
</script>

<style scoped></style>
