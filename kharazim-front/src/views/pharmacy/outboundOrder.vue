<template>
  <div>
    <el-form :inline="true" :model="pageRequest" class="page-form-block" @keyup.enter="loadOutboundOrders">
      <el-form-item label="出库单编码">
        <el-input v-model="pageRequest.code" clearable placeholder="出库单编码" />
      </el-form-item>
      <el-form-item label="来源单据编码">
        <el-input v-model="pageRequest.sourceBusinessCode" clearable placeholder="来源单据编码" />
      </el-form-item>
      <el-form-item label="会员">
        <el-select
          v-model="pageRequest.customerCode"
          :remote-method="async (query: any) => (customers = await loadSimpleCustomers(query))"
          clearable
          filterable
          placeholder="选择会员"
          remote
          width="500px"
        >
          <el-option v-for="item in customers" :key="item.code" :label="item.name" :value="item.code">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right">{{ item.phone }}</span>
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
          <el-option v-for="item in clinicOptions" :key="item.code" :label="item.name" :value="item.code" />
        </el-select>
      </el-form-item>
      <el-form-item label="出库状态">
        <el-select v-model="pageRequest.status" clearable filterable placeholder="选择出库状态" reserve-keyword>
          <el-option
            v-for="option in outboundStatusOptions"
            :key="option.key"
            :label="option.value"
            :value="option.key"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadOutboundOrders">查询</el-button>
        <el-button type="primary" @click="resetAndLoadOutboundOrders">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <div>
      <el-table :data="outboundOrderPageData.data" border style="width: 100%; margin-top: 10px">
        <el-table-column type="expand">
          <template v-slot="{ row }">
            <div style="padding: 10px 50px 10px 50px">
              <el-table :data="row.items" border>
                <el-table-column align="center" type="index" width="50" />
                <el-table-column align="center" label="商品主图" width="120">
                  <template v-slot="{ row: item }">
                    <el-image v-if="fileUrl(item.defaultImage)" :src="fileUrl(item.defaultImage)" style="width: 50px" />
                  </template>
                </el-table-column>
                <el-table-column label="商品编码" prop="skuCode" />
                <el-table-column label="商品名称" prop="skuName" />
                <el-table-column label="单位" prop="unitName" width="80" />
                <el-table-column label="商品分类" prop="categoryName" />
                <el-table-column align="center" label="数量" prop="quantity" />
              </el-table>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="出库单编码" prop="code" width="160" />
        <el-table-column label="来源业务编码" prop="sourceBusinessCode" width="160" />
        <el-table-column label="诊所" prop="clinicName" width="160" />
        <el-table-column align="center" label="出库状态" prop="statusName" width="100">
          <template v-slot="{ row }">
            <el-text v-if="row.status === 'OUTBOUND_FINISHED'" type="success">
              {{ row.statusName }}
            </el-text>
            <el-text v-if="row.status === 'WAIT_OUTBOUND'" type="primary">
              {{ row.statusName }}
            </el-text>
          </template>
        </el-table-column>
        <el-table-column label="会员姓名" prop="customerName" width="160" />
        <el-table-column align="center" label="操作" width="100">
          <template v-slot="{ row }">
            <el-button
              v-if="row.status !== 'OUTBOUND_FINISHED'"
              size="small"
              type="primary"
              @click="markOutboundFinished(row)"
            >
              标记出库
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="来源备注" min-width="160" prop="sourceRemark" />
      </el-table>
    </div>
    <div class="pagination-block">
      <el-pagination
        v-model:current-page="pageRequest.pageIndex"
        v-model:page-size="pageRequest.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="outboundOrderPageData.totalCount"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadOutboundOrders"
        @current-change="loadOutboundOrders"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, toRaw } from "vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";
import { fileUrl } from "@/utils/fileUrl.ts";
import { ElMessage, ElMessageBox } from "element-plus";
import { ClinicVO, loadClinics } from "@/views/clinic/clinicManagement.vue";
import { loadSimpleCustomers, SimpleCustomer } from "@/views/customer/customer-list";
import { DictOption, loadDictOptions } from "@/views/dict/dict-item";

const outboundOrderPageData = ref({ totalCount: 0, data: [] });

const initPageRequest = {
  code: "",
  sourceBusinessCode: "",
  clinicCodes: [],
  customerCode: "",
  status: "",
  pageIndex: 1,
  pageSize: 10,
};

const pageRequest = reactive({ ...initPageRequest });

function loadOutboundOrders() {
  axios.get("/kharazim-api/outbound-order/page", { params: toRaw(pageRequest) }).then((response: AxiosResponse) => {
    outboundOrderPageData.value = response.data;
  });
}

function resetAndLoadOutboundOrders() {
  Object.assign(pageRequest, initPageRequest);
  loadOutboundOrders();
}

const customers = ref<SimpleCustomer[]>([]);
const clinicOptions = ref<ClinicVO[]>([]);
const outboundStatusOptions = ref<DictOption[]>([]);

function markOutboundFinished(row: any) {
  ElMessageBox.confirm("确认已收货？", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      axios
        .post("/kharazim-api/outbound-order/outbound", {
          outboundOrderCode: row.code,
        })
        .then(() => {
          ElMessage({
            type: "success",
            message: "收货成功",
          });
          loadOutboundOrders();
        });
    })
    .catch(() => {});
  console.log(row);
}

onMounted(() => {
  loadDictOptions("outbound_order_status").then((res: DictOption[]) => (outboundStatusOptions.value = res));
  loadClinics().then((res: ClinicVO[]) => (clinicOptions.value = res));
  loadOutboundOrders();
});
</script>

<style scoped></style>
