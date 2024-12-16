<template>
  <div>
    <el-button @click="showAddFamilyDialog">新建家庭</el-button>
  </div>
  <div>
    <div>
      <el-table :data="familyPageData" border style="margin-top: 10px">
        <el-table-column label="家庭编码" prop="familyCode" width="200" />
        <el-table-column label="家庭名称" prop="familyName" />
        <el-table-column label="家庭户主" prop="leaderCustomerName" />
        <el-table-column label="备注信息" prop="remark" />
        <el-table-column label="操作">
          <template v-slot="{ row }">
            <el-button size="small" @click="showFamilyMembers(row)"
              >查看成员
            </el-button>
            <el-button size="small" @click="showAddFamilyMembers(row)"
              >添加成员
            </el-button>
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
        @size-change="loadFamily"
        @current-change="loadFamily"
      />
    </div>
  </div>
  <el-dialog v-model="familyMembersVisible" title="家庭成员" width="800">
    <el-table :data="familyMembers">
      <el-table-column label="会员名" property="name" width="150" />
      <el-table-column
        label="与户主关系"
        property="relationToLeader"
        width="200"
      />
      <el-table-column label="电话" property="phone" />
    </el-table>
  </el-dialog>
  <el-dialog v-model="addFamilyVisible" title="新建家庭" width="500">
    <el-form :model="addFamilyData" label-width="15%">
      <el-form-item label="家庭名称">
        <el-input
          v-model="addFamilyData.familyName"
          autocomplete="off"
          placeholder="请输入家庭名称"
        />
      </el-form-item>
      <el-form-item label="家庭户主">
        <el-select
          v-model="addFamilyData.leaderCustomerCode"
          :remote-method="
            async (query: any) => (customers = await loadSimpleCustomers(query))
          "
          filterable
          placeholder="选择户主"
          remote
        >
          <el-option
            v-for="item in customers"
            :key="item.code"
            :label="item.name"
            :value="item.code"
          >
            <span style="float: left">{{ item.name }}</span>
            <span class="family-leader-select">{{ item.phone }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="备注">
        <el-input
          v-model="addFamilyData.remark"
          autocomplete="off"
          placeholder="备注"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeAddFamilyDialog">取消</el-button>
        <el-button type="primary" @click="confirmAddFamily">确定</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog v-model="addFamilyMembersVisible" title="添加家庭成员" width="500">
    <el-form :model="addFamilyMemberSelected" label-width="20%">
      <el-form-item label="选择成员">
        <el-select
          v-model="addFamilyMemberSelected?.customerCode"
          :remote-method="
            async (query: any) => (customers = await loadSimpleCustomers(query))
          "
          filterable
          placeholder="选择成员"
          remote
        >
          <el-option
            v-for="item in customers"
            :key="item.code"
            :label="item.name"
            :value="item.code"
          >
            <span style="float: left">{{ item.name }}</span>
            <span class="family-leader-select">{{ item.phone }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="与户主关系">
        <el-input
          v-model="addFamilyMemberSelected?.relationToLeader"
          autocomplete="off"
          placeholder="与户主关系"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeAddFamilyMemberDialog">取消</el-button>
        <el-button type="primary" @click="confirmAddFamilyMember"
          >确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import axios from "@/utils/http.js";
import { AxiosResponse } from "axios";
import {
  loadSimpleCustomers,
  SimpleCustomer,
} from "@/views/customer/customer-list";

interface FamilyMember {
  name: string;
  customerCode: string;
  phone: string;
  relationToLeader: string;
}

interface CustomerFamily {
  familyCode: string;
  familyName: string;
  leaderCustomerCode: string;
  leaderCustomerName: string;
  remark: string;
  familyMembers: FamilyMember[] | null;
}

const familyPageData = ref<CustomerFamily[]>([]);
const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [10, 20, 50, 100],
});

const addFamilyVisible = ref(false);
const addFamilyData = ref<CustomerFamily>({
  familyCode: "",
  familyName: "",
  leaderCustomerCode: "",
  leaderCustomerName: "",
  remark: "",
  familyMembers: null,
});

const customers = ref<SimpleCustomer[]>([]);

function showAddFamilyDialog() {
  addFamilyData.value = {
    familyCode: "",
    familyName: "",
    leaderCustomerCode: "",
    leaderCustomerName: "",
    remark: "",
    familyMembers: null,
  };
  addFamilyVisible.value = true;
}

function closeAddFamilyDialog() {
  addFamilyVisible.value = false;
}

function confirmAddFamily() {
  axios
    .post("/kharazim-api/customer/family/create", {
      familyName: addFamilyData.value.familyName,
      leaderCustomerCode: addFamilyData.value.leaderCustomerCode,
      remark: addFamilyData.value.remark,
    })
    .then(() => {
      loadFamily();
      closeAddFamilyDialog();
    });
}

const familyMembersVisible = ref(false);
const familyMembers = ref<FamilyMember[]>([]);

function showFamilyMembers(family: CustomerFamily) {
  familyMembers.value = family.familyMembers || [];
  familyMembersVisible.value = true;
}

const addFamilyMembersVisible = ref(false);
const addFamilyMemberFamily = ref<CustomerFamily>();
const addFamilyMemberSelected = ref<FamilyMember>();

function showAddFamilyMembers(family: CustomerFamily) {
  addFamilyMemberFamily.value = family;
  addFamilyMemberSelected.value = {
    name: "",
    customerCode: "",
    phone: "",
    relationToLeader: "",
  };
  addFamilyMembersVisible.value = true;
}

function closeAddFamilyMemberDialog() {
  addFamilyMembersVisible.value = false;
}

function confirmAddFamilyMember() {
  axios
    .post("/kharazim-api/customer/family/family-member/add", {
      familyCode: addFamilyMemberFamily.value?.familyCode,
      customerCode: addFamilyMemberSelected.value?.customerCode,
      relationToLeader: addFamilyMemberSelected.value?.relationToLeader,
    })
    .then(() => {
      loadFamily();
      closeAddFamilyMemberDialog();
    });
}

function loadFamily() {
  axios
    .get(
      `/kharazim-api/customer/family/page?pageSize=${pageInfo.pageSize}&pageIndex=${pageInfo.currentPage}`,
    )
    .then((response: AxiosResponse) => {
      familyPageData.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
    });
}

onMounted(() => loadFamily());
</script>

<style scoped>
.family-leader-select {
  float: right;
  color: var(--el-text-color-secondary);
  font-size: 13px;
}
</style>
