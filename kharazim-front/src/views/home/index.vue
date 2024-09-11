<template>
  <div id="module" style="height: 100vh">
    <el-container style="height: 100%">
      <NavMenu />
      <el-container>
        <el-header style="margin-top: 15px; height: 30px">
          <div style="display: flex">
            <div style="flex: 2">
              <Bread />
            </div>
            <div style="flex: 3">
              <div class="header-right">
                <div>
                  <el-text>{{ currentUser.nickName }}</el-text>
                </div>
                <div class="current-user-avatar">
                  <el-avatar
                    v-if="currentUser && currentUser.avatarUrl"
                    :src="currentUser.avatarUrl"
                  />
                </div>
                <el-button
                  class="change-password"
                  @click="showChangeCurrentUserPassword"
                  >修改密码
                </el-button>
                <el-button class="logout" @click="logout">注销</el-button>
              </div>
            </div>
          </div>
        </el-header>
        <el-divider style="margin: 16px 0 6px" />
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
  <el-dialog v-model="changePasswordVisible" title="修改密码" width="500">
    <el-form
      ref="changePasswordFormRef"
      :model="changePasswordForm"
      :rules="changePasswordRules"
      label-width="auto"
      status-icon
    >
      <el-form-item label="原密码" prop="oldPassword">
        <el-input
          v-model="changePasswordForm.oldPassword"
          show-password
          type="password"
        />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input
          v-model="changePasswordForm.newPassword"
          show-password
          type="password"
        />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input
          v-model="changePasswordForm.confirmPassword"
          show-password
          type="password"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm"> 确认</el-button>
        <el-button @click="changePasswordVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script lang="ts" setup>
import Bread from "./bread.vue";
import NavMenu from "./navMenu.vue";
import { removeToken } from "@/utils/auth.js";
import { useRouter } from "vue-router";
import { onMounted, reactive, ref } from "vue";
import axios from "@/utils/http.js";
import { AxiosResponse } from "axios";
import type { FormRules } from "element-plus";
import { ElMessage, FormInstance } from "element-plus";

const router = useRouter();

function logout() {
  axios
    .post("/kharazim-api/auth/logout", null, {
      disablePrintGlobalError: true,
    })
    .catch(() => {});
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

const changePasswordVisible = ref(false);

interface ChangePasswordForm {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;
}

const changePasswordFormRef = ref<FormInstance>();
const changePasswordForm = reactive<ChangePasswordForm>({
  oldPassword: "",
  newPassword: "",
  confirmPassword: "",
});

const changePasswordRules = reactive<FormRules<ChangePasswordForm>>({
  oldPassword: [
    { required: true, message: "请输入原密码", trigger: "blur" },
    { min: 6, max: 32, message: "密码长度必须在6~32之间", trigger: "blur" },
  ],
  newPassword: [
    { required: true, message: "请输入新密码", trigger: "blur" },
    { min: 6, max: 32, message: "密码长度必须在6~32之间", trigger: "blur" },
  ],
  confirmPassword: [
    { required: true, message: "请再次输入新密码", trigger: "blur" },
    { min: 6, max: 32, message: "密码长度必须在6~32之间", trigger: "blur" },
    { validator: checkConfirmPassword, trigger: "blur" },
  ],
});

function checkConfirmPassword(rule: any, value: any, callback: any) {
  if (value !== changePasswordForm.newPassword) {
    callback(new Error("确认密码必须与新密码相同错误"));
  } else {
    callback();
  }
}

const submitForm = async () => {
  if (changePasswordFormRef.value) {
    await changePasswordFormRef.value.validate((valid) => {
      if (valid) {
        axios
          .post("/kharazim-api/user/change-password", {
            oldPassword: changePasswordForm.oldPassword,
            newPassword: changePasswordForm.newPassword,
          })
          .then(() => {
            ElMessage({
              message: "修改密码成功!",
              type: "success",
            });
            setTimeout(() => {
              logout();
            }, 1000);
          });
      }
    });
  }
};

function showChangeCurrentUserPassword() {
  if (changePasswordFormRef.value) {
    changePasswordFormRef.value.resetFields();
  }
  changePasswordVisible.value = true;
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
  margin-right: 10px;
  padding: 10px;
}

.change-password {
  margin-left: 20px;
}
</style>
