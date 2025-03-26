<template>
  <div>
    <el-form :inline="true" :model="pageRequest" class="page-form-block" @keyup.enter="loadInventoryLogs">
      <el-form-item label="商品编码">
        <el-input v-model="pageRequest.skuCode" clearable placeholder="商品编码" />
      </el-form-item>
      <el-form-item label="诊所">
        <el-select v-model="pageRequest.clinicCode" clearable filterable placeholder="选择诊所" reserve-keyword>
          <el-option v-for="item in clinicOptions" :key="item.code" :label="item.name" :value="item.code" />
        </el-select>
      </el-form-item>
      <el-form-item label="库存变化类型">
        <el-select
          v-model="pageRequest.changeTypes"
          clearable
          filterable
          multiple
          placeholder="库存变化类型"
          reserve-keyword
        >
          <el-option v-for="option in changeTypeOptions" :key="option.key" :label="option.value" :value="option.key" />
        </el-select>
      </el-form-item>
      <el-form-item label="关联业务编码">
        <el-input v-model="pageRequest.businessCode" clearable placeholder="关联业务编码" />
      </el-form-item>
      <el-form-item label="时间">
        <el-date-picker
          v-model="pageRequest.timeRange"
          end-placeholder="截止时间"
          start-placeholder="开始时间"
          style="width: 280px"
          type="datetimerange"
        />
      </el-form-item>
      <el-form-item class="page-form-block-search-block">
        <el-button type="primary" @click="loadInventoryLogs">查询</el-button>
        <el-button type="primary" @click="resetAndLoadInventoryLogs">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <div>
      <el-table :data="inventoryLogPageData.data" border style="width: 100%; margin-top: 10px">
        <el-table-column align="center" label="ID" prop="id" width="70" />
        <el-table-column label="流水批次号" prop="serialCode" width="170" />
        <el-table-column label="商品编码" prop="skuCode" width="150" />
        <el-table-column align="center" label="诊所" prop="clinicName" width="140" />
        <el-table-column label="关联业务编码" prop="businessCode" width="170" />
        <el-table-column align="center" label="类型" prop="changeTypeName" width="100" />
        <el-table-column align="center" label="变化数量" prop="quantity" width="90" />
        <el-table-column align="center" label="结存数量" prop="balanceQuantity" width="90" />
        <el-table-column align="center" label="结存预占数量" prop="balanceOccupyQuantity" width="110" />
        <el-table-column align="center" label="时间" prop="operateTime" width="170" />
        <el-table-column align="center" label="操作人" min-width="100" prop="operator" />
        <el-table-column label="SKU名称" min-width="160" prop="skuName" />
        <el-table-column align="center" label="单位" prop="unitName" width="80" />
        <el-table-column align="center" label="商品主图">
          <template v-slot="{ row }">
            <el-image v-if="fileUrl(row.defaultImage)" :src="fileUrl(row.defaultImage)" style="width: 40px"/>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination-block">
      <el-pagination
        v-model:current-page="pageRequest.pageIndex"
        v-model:page-size="pageRequest.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="inventoryLogPageData.totalCount"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadInventoryLogs"
        @current-change="loadInventoryLogs"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, toRaw } from "vue";
import axios from "@/utils/http.js";
import { AxiosResponse } from "axios";
import { dateTimeFormat } from "@/utils/DateUtil";
import { ClinicVO, loadClinics } from "@/views/clinic/clinicManagement.vue";
import { DictOption, loadDictOptions } from "@/views/dict/dict-item";
import { fileUrl } from "@/utils/fileUrl";

const inventoryLogPageData = ref({ totalCount: 0, data: [] });

const initPageRequest = {
  skuCode: "",
  businessCode: "",
  changeTypes: [],
  clinicCode: "",
  timeRange: [] as Date[],
  startTime: "",
  endTime: "",
  pageIndex: 1,
  pageSize: 10,
};

const pageRequest = reactive({ ...initPageRequest });

function loadInventoryLogs() {
  let pageParams = toRaw(pageRequest);
  if (pageRequest.timeRange && pageRequest.timeRange.length === 2) {
    pageParams.startTime = dateTimeFormat(pageRequest.timeRange[0]);
    pageParams.endTime = dateTimeFormat(pageRequest.timeRange[1]);
  } else {
    pageParams.startTime = "";
    pageParams.endTime = "";
  }

  axios
    .get("/kharazim-api/inventory/page-log", { params: pageParams })
    .then((response: AxiosResponse) => (inventoryLogPageData.value = response.data));
}

function resetAndLoadInventoryLogs() {
  Object.assign(pageRequest, initPageRequest);
  loadInventoryLogs();
}

const changeTypeOptions = ref<DictOption[]>([]);
const clinicOptions = ref<ClinicVO[]>([]);

onMounted(() => {
  loadDictOptions("inventory_change_type").then((res: DictOption[]) => (changeTypeOptions.value = res));
  loadClinics().then((res: ClinicVO[]) => (clinicOptions.value = res));
  loadInventoryLogs();
});
</script>

<style scoped />
