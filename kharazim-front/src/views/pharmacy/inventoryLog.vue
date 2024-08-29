<template>
  <div>
    <el-form
      :inline="true"
      :model="pageRequest"
      class="page-form-block"
      @keyup.enter="loadInventoryLogs"
    >
      <el-form-item label="商品编码">
        <el-input
          v-model="pageRequest.skuCode"
          clearable
          placeholder="商品编码"
        />
      </el-form-item>
      <el-form-item label="诊所">
        <el-select
          v-model="pageRequest.clinicCode"
          clearable
          filterable
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
      <el-form-item label="时间">
        <el-date-picker
          v-model="pageRequest.timeRange"
          end-placeholder="截止时间"
          start-placeholder="开始时间"
          type="datetimerange"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadInventoryLogs">查询</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <div>
      <el-table
        :data="inventoryLogPageData"
        border
        style="width: 100%; margin-top: 10px"
      >
        <el-table-column label="流水号" prop="id" width="100" />
        <el-table-column
          label="关联业务编码"
          prop="sourceBusinessCode"
          width="170"
        />
        <el-table-column label="诊所" prop="clinicName" width="160" />
        <el-table-column label="SKU编码" prop="skuCode" width="160" />
        <el-table-column
          align="center"
          label="类型"
          prop="changeTypeName"
          width="100"
        />
        <el-table-column
          align="center"
          label="变化数量"
          prop="quantity"
          width="100"
        />
        <el-table-column
          align="center"
          label="结存数量"
          prop="balanceQuantity"
          width="100"
        />
        <el-table-column
          align="center"
          label="结存预占数量"
          prop="balanceOccupyQuantity"
          width="110"
        />
        <el-table-column
          align="center"
          label="时间"
          prop="operateTime"
          width="170"
        />
        <el-table-column
          align="center"
          label="操作人"
          prop="operator"
          width="100"
        />
        <el-table-column label="SKU名称" min-width="160" prop="skuName" />
        <el-table-column
          align="center"
          label="单位"
          prop="unitName"
          width="80"
        />
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
        @size-change="loadInventoryLogs"
        @current-change="loadInventoryLogs"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import axios from "@/utils/http.js";
import { AxiosResponse } from "axios";
import { dateTimeFormat } from "@/utils/DateUtil";

const inventoryLogPageData = ref([]);

const pageRequest = reactive({
  skuCode: "",
  clinicCode: "",
  timeRange: [] as Date[],
});

const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [10, 20, 50, 100],
});

function loadInventoryLogs() {
  let startTime = "";
  let endTime = "";
  if (pageRequest.timeRange && pageRequest.timeRange.length === 2) {
    startTime = dateTimeFormat(pageRequest.timeRange[0]);
    endTime = dateTimeFormat(pageRequest.timeRange[1]);
  }

  axios
    .get("/kharazim-api/inventory/page-log", {
      params: {
        skuCode: pageRequest.skuCode,
        clinicCode: pageRequest.clinicCode,
        startTime: startTime,
        endTime: endTime,
        pageSize: pageInfo.pageSize,
        pageNum: pageInfo.currentPage,
      },
    })
    .then((response: AxiosResponse) => {
      inventoryLogPageData.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
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
  loadInventoryLogs();
  loadClinicOptions();
});
</script>

<style scoped></style>
