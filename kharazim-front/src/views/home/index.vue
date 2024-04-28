<template>
  <div id="module">
    <el-container>
      <NavMenu />
      <el-container>
        <el-header>
          <Bread />
          <div class="header-right">
            <el-text>{{ currentUser.nickName }}</el-text>
            <div class="current-user-avatar">
              <el-avatar
                v-if="currentUser && currentUser.avatarUrl"
                :src="currentUser.avatarUrl"
              />
            </div>
            <el-button class="logout" @click="logout">注销</el-button>
          </div>
        </el-header>
        <el-divider />
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script lang="ts" setup>
import Bread from "./bread.vue";
import NavMenu from "./navMenu.vue";
import { removeToken } from "@/utils/auth.js";
import { useRouter } from "vue-router";
import { onMounted, ref } from "vue";
import axios from "@/utils/http.js";
import { AxiosResponse } from "axios";

const router = useRouter();

function logout() {
  removeToken();
  router.push("/login");
}

const currentUser = ref({
  id: 0,
  code: "",
  name: "",
  nickName: "",
  englishName: "",
  avatar: "",
  avatarUrl: "",
  gender: "",
  birthday: "",
  phone: "",
  roleName: "",
  needChangePassword: false,
  lastLogin: "",
});

function loadCurrentUser() {
  axios.get("/kharazim-api/user/current-user").then((res: AxiosResponse) => {
    currentUser.value = res.data.data;
  });
}

onMounted(() => loadCurrentUser());
</script>

<style scoped>
.current-user-avatar {
  margin-left: 20px;
}

.header-right {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.logout {
  margin-left: 20px;
  margin-right: 20px;
  padding: 10px;
}
</style>
