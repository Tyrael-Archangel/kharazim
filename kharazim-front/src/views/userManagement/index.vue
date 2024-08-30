<template>
  <el-form :inline="true" :model="pageRequest" class="page-form-block">
    <el-form-item label="关键字">
      <el-input
        v-model="pageRequest.keywords"
        clearable
        placeholder="用户名/昵称/手机号"
      />
    </el-form-item>
    <el-form-item label="状态">
      <el-select v-model="pageRequest.status" clearable placeholder="状态">
        <el-option label="已启用" value="ENABLED" />
        <el-option label="已禁用" value="DISABLED" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="loadUser">查询</el-button>
    </el-form-item>
  </el-form>

  <div>
    <el-table :data="userPageData" border style="width: 100%">
      <el-table-column label="用户编码" prop="code" width="100" />
      <el-table-column label="用户名" prop="name" />
      <el-table-column align="center" label="头像" width="80">
        <template v-slot="{ row }">
          <el-avatar v-if="row.avatarUrl" :src="row.avatarUrl" />
        </template>
      </el-table-column>
      <el-table-column label="用户昵称" prop="nickName" />
      <el-table-column label="性别" width="65">
        <template v-slot="{ row }">
          <el-icon :color="'MALE' === row.gender ? 'blue' : 'red'" size="22">
            <component :is="'MALE' === row.gender ? 'Male' : 'Female'" />
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column label="出生日期" prop="birthday" width="120" />
      <el-table-column label="角色" prop="roleName" />
      <el-table-column label="状态" prop="status" width="65">
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
          <el-link :underline="false" type="primary" @click="resetPwd(row)"
            >重置密码
          </el-link>
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
      @size-change="loadUser"
      @current-change="loadUser"
    />
  </div>
</template>

<script lang="ts" setup>
import axios from "@/utils/http.js";

import { onMounted, reactive, ref } from "vue";
import { AxiosResponse } from "axios";
import { ElMessageBox } from "element-plus";

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
  roleId: number;
  roleCode: string;
  roleName: string;
  creator: string;
  creatorCode: string;
  createTime: string;
  updater: string;
  updaterCode: string;
  updateTime: string;
}

const userPageData = ref<UserData[]>([]);
const pageInfo = reactive({
  currentPage: 1,
  pageSize: 10,
  totalCount: 0,
  pageSizes: [10, 20, 50, 100],
});

function switchStatus(user: UserData) {
  axios
    .put(`/kharazim-api/user/update-status/${user.id}/${user.status}`)
    .then(() => loadUser());
}

const pageRequest = reactive({
  keywords: "",
  status: "",
});

function loadUser() {
  axios
    .get(
      `/kharazim-api/user/page?pageSize=${pageInfo.pageSize}&pageNum=${pageInfo.currentPage}` +
        `${pageRequest.keywords ? "&keywords=" + pageRequest.keywords : ""}` +
        `&status=${pageRequest.status ? "&status=" + pageRequest.status : ""}`,
    )
    .then((response: AxiosResponse) => {
      userPageData.value = response.data.data;
      pageInfo.totalCount = response.data.totalCount;
    });
}

function resetPwd(user: UserData) {
  ElMessageBox.confirm(`确认重置用户 ${user.nickName} 的密码?`, "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      axios
        .post(`/kharazim-api/user/reset-password/${user.id}`)
        .then((response: AxiosResponse) => {
          let newPassword = response.data.data;
          ElMessageBox.alert(`新密码: ${newPassword}`, "操作成功", {
            confirmButtonText: "确认",
          });
        });
    })
    .catch(() => {});
}

onMounted(() => loadUser());
</script>

<style scoped></style>