<template>
  <div>
    <div>
      <el-table :data="rechargeCardTypePageData" border style="width: 100%; margin-top: 10px">
        <el-table-column label="储值卡项编码" prop="code" />
        <el-table-column label="储值卡项名称" prop="name" />
        <el-table-column label="折扣百分比" prop="discountPercent" />
        <el-table-column label="有效期">
          <template v-slot="{ row }">
            <el-text>{{ expireDays(row) }}</el-text>
          </template>
        </el-table-column>
        <el-table-column label="默认卡金额" prop="defaultAmount" />
        <el-table-column label="是否可发新卡">
          <template v-slot="{ row }">
            <el-switch
              v-model="row.canCreateNewCard"
              :active-value="true"
              :inactive-value="false"
              active-text="可发新卡"
              inactive-text="禁止发卡"
              inline-prompt
              style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
              @change="switchEnableStatus(row)"
            />
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
        @size-change="loadRechargeCardType"
        @current-change="loadRechargeCardType"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";

interface RechargeCardType {
  code: string;
  name: string;
  discountPercent: number;
  neverExpire: boolean;
  validPeriodDays: number;
  canCreateNewCard: boolean;
}

const rechargeCardTypePageData = ref<RechargeCardType[]>([]);
const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [10, 20, 50, 100],
});

function loadRechargeCardType() {
  axios
    .get(`/kharazim-api/recharge-card-type/page?pageSize=${pageInfo.pageSize}&pageIndex=${pageInfo.currentPage}`)
    .then((response: AxiosResponse) => {
      rechargeCardTypePageData.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
    });
}

function expireDays(rechargeCardType: RechargeCardType) {
  if (rechargeCardType.neverExpire) {
    return "永不过期";
  }
  return rechargeCardType.validPeriodDays + "天";
}

function switchEnableStatus(rechargeCardType: RechargeCardType) {
  const url = rechargeCardType.canCreateNewCard
    ? `/kharazim-api/recharge-card-type/enable-create-new-card/${rechargeCardType.code}`
    : `/kharazim-api/recharge-card-type/disable-create-new-card/${rechargeCardType.code}`;
  axios.put(url).then(() => loadRechargeCardType());
}

onMounted(() => loadRechargeCardType());
</script>

<style scoped />
