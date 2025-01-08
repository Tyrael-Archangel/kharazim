<template>
  <div>
    <el-form :inline="true" :model="pageRequest" class="page-form-block">
      <el-form-item>
        <el-button type="primary" @click="loadOnlineUsers">刷新</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <el-table :data="onlineUserPageData.data" border style="width: 100%">
      <el-table-column label="token" prop="token" width="300" />
      <el-table-column label="用户编码" prop="userCode" width="100" />
      <el-table-column label="用户名" prop="username" width="120" />
      <el-table-column align="center" label="头像" width="80">
        <template v-slot="{ row }">
          <el-avatar v-if="row.userAvatarUrl" :src="row.userAvatarUrl" />
        </template>
      </el-table-column>
      <el-table-column label="登录时间" prop="loginTime" width="170" />
      <el-table-column label="最后访问时间" prop="lastRefreshTime" width="170" />
      <el-table-column label="登录主机" prop="host" />
      <el-table-column label="操作系统" prop="os" />
      <el-table-column label="浏览器" prop="browser" />
      <el-table-column label="浏览器版本" prop="browserVersion" />
      <el-table-column align="center" label="操作" width="100">
        <template v-slot="{ row }">
          <el-link :underline="false" type="primary" @click="forceLogout(row)">强制退出</el-link>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <div class="pagination-block">
    <el-pagination
      v-model:current-page="pageRequest.pageIndex"
      v-model:page-size="pageRequest.pageSize"
      :page-sizes="[10, 20, 50, 100]"
      :total="onlineUserPageData.totalCount"
      background
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="loadOnlineUsers"
      @current-change="loadOnlineUsers"
    />
  </div>
</template>

<script lang="ts" setup>
import axios from "@/utils/http.js";
import { onMounted, reactive, ref, toRaw } from "vue";
import { AxiosResponse } from "axios";
import { ElMessage } from "element-plus";

const initPageRequest = {
  pageIndex: 1,
  pageSize: 10,
};

const pageRequest = reactive({ ...initPageRequest });

interface OnlineUserData {
  token: number;
  userCode: number;
  userNickName: number;
  userAvatar: number;
  userAvatarUrl: number;
  loginTime: string;
  lastRefreshTime: string;
  host: number;
  os: number;
  browser: number;
  browserVersion: number;
}

const onlineUserPageData = ref({ totalCount: 0, data: [] as OnlineUserData[] });

function loadOnlineUsers() {
  axios
    .get("/kharazim-api/auth/online-users", { params: toRaw(pageRequest) })
    .then((response: AxiosResponse) => (onlineUserPageData.value = response.data));
}

function forceLogout(onlineUserData: OnlineUserData) {
  axios.put("kharazim-api/auth/force-logout", null, { params: { token: onlineUserData.token } }).then(() => {
    ElMessage.success("操作成功");
    loadOnlineUsers();
  });
}

onMounted(() => {
  loadOnlineUsers();
});
</script>

<style scoped></style>
