<template>
  <div>
    <el-form
      :inline="true"
      :model="pageRequest"
      class="page-form-block"
      @keyup.enter="loadCommunicationLogs"
    >
      <el-form-item label="会员">
        <el-select
          v-model="pageRequest.customerCode"
          :remote-method="
            async (query: any) => (customers = await loadSimpleCustomers(query))
          "
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
            <span style="float: right">{{ item.phone }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="客服人员">
        <el-select
          v-model="pageRequest.serviceUserCode"
          :remote-method="
            async (query: any) => (serviceUsers = await loadSimpleUsers(query))
          "
          clearable
          filterable
          placeholder="选择客服人员"
          remote
          width="500px"
        >
          <el-option
            v-for="item in serviceUsers"
            :key="item.code"
            :label="item.nickName"
            :value="item.code"
          >
            <el-image
              v-if="item.avatarUrl"
              :src="item.avatarUrl"
              style="float: left; width: 30px"
            />
            <span style="float: right">{{ item.nickName }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="沟通类型">
        <el-select
          v-model="pageRequest.typeDictKey"
          clearable
          placeholder="沟通类型"
        >
          <el-option
            v-for="option in typeDictOptions"
            :key="option.key"
            :label="option.value"
            :value="option.key"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="沟通评价">
        <el-select
          v-model="pageRequest.evaluateDictKey"
          clearable
          placeholder="沟通评价"
        >
          <el-option
            v-for="option in evaluateDictOptions"
            :key="option.key"
            :label="option.value"
            :value="option.key"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="pageRequest.createdTime"
          end-placeholder="截止时间"
          start-placeholder="开始时间"
          style="width: 280px"
          type="datetimerange"
        />
      </el-form-item>
      <el-form-item label="沟通时间">
        <el-date-picker
          v-model="pageRequest.communicationTime"
          end-placeholder="截止时间"
          start-placeholder="开始时间"
          style="width: 280px"
          type="datetimerange"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadCommunicationLogs">
          查询
        </el-button>
        <el-button type="primary" @click="resetAndLoadCommunicationLogs">
          重置
        </el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <el-button type="primary" @click="showAddLogDialog">添加沟通记录</el-button>
  </div>
  <div>
    <div>
      <el-table
        :data="communicationPageData.data"
        border
        style="width: 100%; margin-top: 10px"
      >
        <el-table-column type="index" width="50" />
        <el-table-column label="会员姓名" prop="customerName" width="120" />
        <el-table-column label="沟通类型" prop="type" width="150" />
        <el-table-column label="沟通评价" prop="evaluate" width="160" />
        <el-table-column label="客服人员" prop="serviceUserName" width="130" />
        <el-table-column
          label="沟通时间"
          prop="communicationTime"
          width="170"
        />
        <el-table-column label="创建时间" prop="createTime" width="170" />
        <el-table-column label="创建人" prop="creator" width="130" />
        <el-table-column label="沟通内容" min-width="160" prop="content" />
      </el-table>
    </div>
    <div class="pagination-block">
      <el-pagination
        v-model:current-page="pageRequest.pageIndex"
        v-model:page-size="pageRequest.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="communicationPageData.totalCount"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadCommunicationLogs"
        @current-change="loadCommunicationLogs"
      />
    </div>
  </div>
  <el-dialog v-model="addLogVisible" title="添加沟通记录" width="800">
    <el-form :model="addCommunicationLogData" label-width="12%">
      <el-form-item label="会员">
        <el-select
          v-model="addCommunicationLogData.customerCode"
          :remote-method="
            async (query: any) => (customers = await loadSimpleCustomers(query))
          "
          clearable
          filterable
          placeholder="选择会员"
          remote
        >
          <el-option
            v-for="item in customers"
            :key="item.code"
            :label="item.name + (item.phone ? '（' + item.phone + '）' : '')"
            :value="item.code"
          >
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right">{{ item.phone }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="客服人员">
        <el-select
          v-model="addCommunicationLogData.serviceUserCode"
          :remote-method="
            async (query: any) => (serviceUsers = await loadSimpleUsers(query))
          "
          clearable
          filterable
          placeholder="选择客服人员"
          remote
          width="500px"
        >
          <el-option
            v-for="item in serviceUsers"
            :key="item.code"
            :label="item.nickName"
            :value="item.code"
          >
            <el-image
              v-if="item.avatarUrl"
              :src="item.avatarUrl"
              style="float: left; width: 30px"
            />
            <span style="float: right">{{ item.nickName }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="沟通类型">
        <el-select
          v-model="addCommunicationLogData.typeDictKey"
          clearable
          placeholder="沟通类型"
        >
          <el-option
            v-for="option in typeDictOptions"
            :key="option.key"
            :label="option.value"
            :value="option.key"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="沟通评价">
        <el-select
          v-model="addCommunicationLogData.evaluateDictKey"
          clearable
          placeholder="沟通评价"
        >
          <el-option
            v-for="option in evaluateDictOptions"
            :key="option.key"
            :label="option.value"
            :value="option.key"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="沟通时间">
        <el-date-picker
          v-model="addCommunicationLogData.communicationTime"
          placeholder="沟通时间"
          style="width: 100%"
          type="datetime"
        />
      </el-form-item>
      <el-form-item label="沟通内容">
        <el-input
          v-model="addCommunicationLogData.content"
          :autosize="{ minRows: 5 }"
          :maxlength="1000"
          autocomplete="off"
          placeholder="沟通内容"
          show-word-limit
          type="textarea"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeAddLogDialog">取消</el-button>
        <el-button type="primary" @click="confirmAddCommunicationLog">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, toRaw } from "vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";
import { loadSimpleUsers, SimpleUser } from "@/views/user/user-list";
import { DictOption, loadDictOptions } from "@/views/dict/dict-item";
import {
  loadSimpleCustomers,
  SimpleCustomer,
} from "@/views/customer/customer-list";
import { dateTimeFormat } from "@/utils/DateUtil";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";

const communicationPageData = ref({ totalCount: 0, data: [] });

const initPageRequest = {
  customerCode: "",
  serviceUserCode: "",
  typeDictKey: "",
  evaluateDictKey: "",
  createdBegin: "",
  createdEnd: "",
  createdTime: [] as Date[],
  communicationBegin: "",
  communicationEnd: "",
  communicationTime: [] as Date[],
  pageIndex: 1,
  pageSize: 10,
};

const pageRequest = reactive({ ...initPageRequest });

function loadCommunicationLogs() {
  pageRequest.createdBegin =
    pageRequest.createdEnd =
    pageRequest.communicationBegin =
    pageRequest.communicationEnd =
      "";
  const createdTimePair = pageRequest.createdTime;
  const communicationTimePair = pageRequest.communicationTime;
  if (createdTimePair && createdTimePair.length === 2) {
    pageRequest.createdBegin = dateTimeFormat(createdTimePair[0]);
    pageRequest.createdEnd = dateTimeFormat(createdTimePair[1]);
  }
  if (communicationTimePair && communicationTimePair.length === 2) {
    pageRequest.communicationBegin = dateTimeFormat(communicationTimePair[0]);
    pageRequest.communicationEnd = dateTimeFormat(communicationTimePair[1]);
  }

  const { createdTime, communicationTime, ...pageParams } = toRaw(pageRequest);
  axios
    .get("/kharazim-api/customer/communication/page", { params: pageParams })
    .then((response: AxiosResponse) => {
      communicationPageData.value = response.data;
    });
}

function resetAndLoadCommunicationLogs() {
  Object.assign(pageRequest, initPageRequest);
  loadCommunicationLogs();
}

const typeDictOptions = ref<DictOption[]>([]);
const evaluateDictOptions = ref<DictOption[]>([]);
const customers = ref<SimpleCustomer[]>([]);
const serviceUsers = ref<SimpleUser[]>([]);

const addLogVisible = ref(false);
const initAddCommunicationLogData = {
  customerCode: "",
  serviceUserCode: "",
  typeDictKey: "",
  evaluateDictKey: "",
  communicationTime: "",
  content: "",
};
const addCommunicationLogData = reactive({ ...initAddCommunicationLogData });

function showAddLogDialog() {
  Object.assign(addCommunicationLogData, initAddCommunicationLogData);
  addLogVisible.value = true;
}

function closeAddLogDialog() {
  addLogVisible.value = false;
}

function confirmAddCommunicationLog() {
  const addLogParams = toRaw(addCommunicationLogData);
  addLogParams.communicationTime = dateTimeFormat(
    addLogParams.communicationTime,
  );
  axios.post("/kharazim-api/customer/communication", addLogParams).then(() => {
    ElMessage.success("操作成功");
    loadCommunicationLogs();
    closeAddLogDialog();
  });
}

const router = useRouter();

onMounted(async () => {
  typeDictOptions.value = await loadDictOptions("communication_type");
  evaluateDictOptions.value = await loadDictOptions("communication_evaluate");

  const query = router.currentRoute.value.query;
  const customerCode = query.customerCode as string;
  if (customerCode && customerCode.length > 0) {
    customers.value = await loadSimpleCustomers("");
    pageRequest.customerCode = customerCode;
  }

  loadCommunicationLogs();
});
</script>

<style scoped></style>
