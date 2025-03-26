<template>
  <el-form :inline="true" :model="pageRequest" class="page-form-block">
    <el-form-item label="关键字">
      <el-input v-model="pageRequest.keywords" clearable placeholder="用户名/昵称/手机号" />
    </el-form-item>
    <el-form-item label="状态">
      <el-select v-model="pageRequest.status" clearable placeholder="状态">
        <el-option v-for="option in statusOptions" :key="option.key" :label="option.value" :value="option.key" />
      </el-select>
    </el-form-item>
    <el-form-item label="性别">
      <el-select v-model="pageRequest.gender" clearable placeholder="性别">
        <el-option v-for="option in genderOptions" :key="option.key" :label="option.value" :value="option.key" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="loadUser">查询</el-button>
      <el-button type="primary" @click="resetAndLoadUser">重置</el-button>
    </el-form-item>
  </el-form>

  <div>
    <el-table :data="userPageData.data" border style="width: 100%">
      <el-table-column label="用户编码" prop="code" width="100" />
      <el-table-column label="用户名" min-width="120" prop="name" />
      <el-table-column align="center" label="头像" width="80">
        <template v-slot="{ row }">
          <el-avatar v-if="fileUrl(row.avatar)" :src="fileUrl(row.avatar)" />
        </template>
      </el-table-column>
      <el-table-column label="用户昵称" min-width="120" prop="nickName" />
      <el-table-column align="center" label="性别" width="58">
        <template v-slot="{ row }">
          <el-icon :color="'MALE' === row.gender ? 'blue' : 'red'" size="22">
            <component :is="'MALE' === row.gender ? 'Male' : 'Female'" />
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column label="电话号码" prop="phone" width="120" />
      <el-table-column label="出生日期" prop="birthday" width="120" />
      <el-table-column align="center" label="角色" width="160">
        <template v-slot="{ row }">
          {{ row.roles.map((r: RoleData) => r.name).join(", ") }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="65">
        <template v-slot="{ row }">
          <el-switch
            v-model="row.status"
            active-value="ENABLED"
            inactive-value="DISABLED"
            @change="switchStatus(row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="创建人" prop="creator" />
      <el-table-column label="创建时间" prop="createTime" width="170" />
      <el-table-column label="更新人" prop="updater" />
      <el-table-column label="更新时间" prop="updateTime" width="170" />
      <el-table-column label="备注" prop="remark" />
      <el-table-column label="操作">
        <template v-slot="{ row }">
          <el-link :underline="false" type="primary" @click="resetPwd(row)">重置密码</el-link>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <div class="pagination-block">
    <el-pagination
      v-model:current-page="pageRequest.pageIndex"
      v-model:page-size="pageRequest.pageSize"
      :page-sizes="[10, 20, 50, 100]"
      :total="userPageData.totalCount"
      background
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="loadUser"
      @current-change="loadUser"
    />
  </div>
</template>

<script lang="ts" setup>
import axios from "@/utils/http.js";
import { fileUrl } from "@/utils/fileUrl.ts";
import { DictOption, loadDictOptions } from "@/views/dict/dict-item";

import { onMounted, reactive, ref, toRaw } from "vue";
import { AxiosResponse } from "axios";
import { ElMessageBox } from "element-plus";

const statusOptions = ref<DictOption[]>([]);
const genderOptions = ref<DictOption[]>([]);

interface RoleData {
  code: string;
  name: string;
}

interface UserData {
  id: number;
  code: string;
  name: string;
  nickName: string;
  englishName: string;
  avatar: string;
  avatarUrl: string;
  gender: string;
  birthday: string;
  phone: string;
  status: string;
  remark: string;
  roles: RoleData[];
  creator: string;
  creatorCode: string;
  createTime: string;
  updater: string;
  updaterCode: string;
  updateTime: string;
}

const userPageData = ref({ totalCount: 0, data: [] as UserData[] });

function switchStatus(user: UserData) {
  axios
    .put(`/kharazim-api/user/update-status/${user.id}/${user.status}`)
    .then(() => loadUser())
    .catch(() => loadUser());
}

const initPageRequest = {
  keywords: "",
  status: "",
  gender: "",
  pageIndex: 1,
  pageSize: 10,
};

const pageRequest = reactive({ ...initPageRequest });

function loadUser() {
  axios
    .get("/kharazim-api/user/page", { params: toRaw(pageRequest) })
    .then((response: AxiosResponse) => (userPageData.value = response.data));
}

function resetAndLoadUser() {
  Object.assign(pageRequest, initPageRequest);
  loadUser();
}

function resetPwd(user: UserData) {
  ElMessageBox.confirm(`确认重置用户 ${user.nickName} 的密码?`, "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      axios.post(`/kharazim-api/user/reset-password/${user.id}`).then((response: AxiosResponse) => {
        let newPassword = response.data.data;
        ElMessageBox.alert(`新密码: ${newPassword}`, "操作成功", {
          confirmButtonText: "确认",
        });
      });
    })
    .catch(() => {});
}

onMounted(() => {
  loadDictOptions("enable_status").then((res: DictOption[]) => (statusOptions.value = res));
  loadDictOptions("user_gender").then((res: DictOption[]) => (genderOptions.value = res));
  loadUser();
});
</script>

<style scoped></style>
