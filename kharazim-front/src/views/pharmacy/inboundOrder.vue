<template>
  <div>
    <el-form
      :inline="true"
      :model="pageRequest"
      class="page-form-block"
      @keyup.enter="loadInboundOrders"
    >
      <el-form-item label="入库单编码">
        <el-input
          v-model="pageRequest.code"
          clearable
          placeholder="入库单编码"
        />
      </el-form-item>
      <el-form-item label="来源单据编码">
        <el-input
          v-model="pageRequest.sourceBusinessCode"
          clearable
          placeholder="来源单据编码"
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
      <el-form-item label="入库状态">
        <el-select
          v-model="pageRequest.status"
          clearable
          filterable
          placeholder="选择入库状态"
          reserve-keyword
        >
          <el-option key="WAIT_INBOUND" label="待入库" value="WAIT_INBOUND" />
          <el-option key="INBOUNDING" label="入库中" value="INBOUNDING" />
          <el-option
            key="INBOUND_FINISHED"
            label="入库完结"
            value="INBOUND_FINISHED"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadInboundOrders">查询</el-button>
        <el-button type="primary" @click="resetAndLoadInboundOrders">
          重置
        </el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <div>
      <el-table
        :data="inboundOrderPageData.data"
        border
        style="width: 100%; margin-top: 10px"
      >
        <el-table-column type="expand">
          <template v-slot="{ row }">
            <div style="padding: 10px 50px 10px 50px">
              <el-table :data="row.items" border>
                <el-table-column align="center" type="index" width="50" />
                <el-table-column align="center" label="商品主图" width="120">
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
                <el-table-column label="单位" prop="unitName" width="80" />
                <el-table-column label="商品分类" prop="categoryName" />
                <el-table-column align="center" label="数量" prop="quantity" />
                <el-table-column
                  align="center"
                  label="已入库数量"
                  prop="inboundedQuantity"
                />
                <el-table-column
                  align="center"
                  label="待入库数量"
                  prop="remainQuantity"
                />
              </el-table>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="入库单编码" prop="code" width="160" />
        <el-table-column
          label="来源业务编码"
          prop="sourceBusinessCode"
          width="160"
        />
        <el-table-column label="诊所" prop="clinicName" width="160" />
        <el-table-column
          align="center"
          label="入库状态"
          prop="statusName"
          width="100"
        >
          <template v-slot="{ row }">
            <el-text v-if="row.status === 'INBOUND_FINISHED'" type="success">
              {{ row.statusName }}
            </el-text>
            <el-text v-if="row.status === 'INBOUNDING'" type="warning">
              {{ row.statusName }}
            </el-text>
            <el-text v-if="row.status === 'WAIT_INBOUND'" type="primary">
              {{ row.statusName }}
            </el-text>
          </template>
        </el-table-column>
        <el-table-column label="供应商" prop="supplierName" width="160" />
        <el-table-column
          align="center"
          label="来源类型"
          prop="sourceTypeName"
          width="100"
        />
        <el-table-column align="center" label="操作" width="100">
          <template v-slot="{ row }">
            <el-button
              :disabled="row.status === 'INBOUND_FINISHED'"
              size="small"
              type="primary"
              @click="showDoInboundDialog(row)"
            >
              入库
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
        :total="inboundOrderPageData.totalCount"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadInboundOrders"
        @current-change="loadInboundOrders"
      />
    </div>
  </div>

  <el-dialog v-model="doInboundVisible" title="新增入库" width="1300">
    <el-table :data="doInboundOrder.items" border max-height="550">
      <el-table-column align="center" type="index" width="50" />
      <el-table-column align="center" label="商品主图" width="100">
        <template v-slot="{ row: item }">
          <el-image
            v-if="item.defaultImageUrl"
            :src="item.defaultImageUrl"
            style="width: 50px"
          >
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="商品编码" prop="skuCode" width="160" />
      <el-table-column label="商品名称" prop="skuName" width="180" />
      <el-table-column label="单位" prop="unitName" width="80" />
      <el-table-column
        align="center"
        label="数量"
        prop="quantity"
        width="100"
      />
      <el-table-column
        align="center"
        label="已入库数量"
        prop="inboundedQuantity"
        width="100"
      />
      <el-table-column
        align="center"
        label="待入库数量"
        prop="remainQuantity"
        width="100"
      />
      <el-table-column
        align="center"
        label="入库数量"
        prop="inboundQuantity"
        width="160"
      >
        <template v-slot="{ row }">
          <el-input-number
            v-model="row.inboundQuantity"
            :max="row.remainQuantity"
            :min="0"
            style="width: 120px"
          />
        </template>
      </el-table-column>
      <el-table-column label="商品分类" min-width="120" prop="categoryName" />
    </el-table>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeDoInboundDialog">取消</el-button>
        <el-button type="primary" @click="confirmAddInbound">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, toRaw } from "vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";
import { ElMessage } from "element-plus";
import { loadSupplierOptions, SupplierVO } from "@/views/supplier/supplier.vue";
import {
  ClinicVO,
  loadClinicOptions,
} from "@/views/clinic/clinicManagement.vue";

const inboundOrderPageData = ref({ totalCount: 0, data: [] });
const initPageRequest = {
  code: "",
  sourceBusinessCode: "",
  clinicCodes: [],
  supplierCodes: [],
  status: "",
  pageIndex: 1,
  pageSize: 10,
};
const pageRequest = reactive({ ...initPageRequest });

const clinicOptions = ref<ClinicVO[]>([]);
const supplierOptions = ref<SupplierVO[]>([]);

function loadInboundOrders() {
  axios
    .get("/kharazim-api/inbound-order/page", { params: toRaw(pageRequest) })
    .then((response: AxiosResponse) => {
      inboundOrderPageData.value = response.data;
    });
}

function resetAndLoadInboundOrders() {
  Object.assign(pageRequest, initPageRequest);
  loadInboundOrders();
}

const doInboundOrder = ref();

const doInboundVisible = ref(false);

function showDoInboundDialog(row: any) {
  doInboundOrder.value = row;
  doInboundVisible.value = true;
}

function closeDoInboundDialog() {
  doInboundVisible.value = false;
}

function confirmAddInbound() {
  if (doInboundOrder.value) {
    const inboundOrderCode = doInboundOrder.value.code;
    let items = doInboundOrder.value.items
      .filter((x: any) => x.inboundQuantity > 0)
      .map((x: any) => {
        return {
          skuCode: x.skuCode,
          quantity: x.inboundQuantity,
        };
      });
    if (items.length > 0) {
      axios
        .post("/kharazim-api/inbound-order/inbound", {
          inboundOrderCode: inboundOrderCode,
          items: items,
        })
        .then(() => {
          ElMessage.success("操作成功");
          loadInboundOrders();
          closeDoInboundDialog();
        });
    }
  }
}

onMounted(async () => {
  clinicOptions.value = await loadClinicOptions();
  supplierOptions.value = await loadSupplierOptions();
  loadInboundOrders();
});
</script>

<style scoped></style>
