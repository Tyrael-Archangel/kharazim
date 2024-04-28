<template>
  <div class="login-block" style="padding-top: 10%; padding-bottom: 2%">
    <el-image src="/kh.svg" style="width: 100px"></el-image>
    <el-text style="font-size: x-large; font-weight: bolder"
      >Kharazim项目
    </el-text>
  </div>
  <div class="login-block">
    <el-form
      ref="formRef"
      :model="loginData"
      class="login-content"
      label-position="right"
      label-width="auto"
    >
      <el-form-item
        :rules="[{ required: true, message: '请输入用户名' }]"
        label="用户名"
        prop="userName"
      >
        <el-input
          v-model="loginData.userName"
          placeholder="用户名"
          @keyup.enter="doLogin(formRef)"
        />
      </el-form-item>
      <el-form-item
        :rules="[{ required: true, message: '请输入密码' }]"
        label="密码"
        prop="password"
      >
        <el-input
          v-model="loginData.password"
          placeholder="密码"
          show-password
          type="password"
          @keyup.enter="doLogin(formRef)"
        />
      </el-form-item>
      <div
        style="display: flex; justify-content: space-around; flex-wrap: nowrap; padding-top: 2%"
      >
        <el-form-item style="width: 60%">
          <div style="width: 50%; padding: 5px; box-sizing: border-box">
            <el-button
              style="width: 100%"
              type="info"
              @click="forgetPwd(formRef)"
              >忘记密码
            </el-button>
          </div>
          <div style="width: 50%; padding: 5px; box-sizing: border-box">
            <el-button
              style="width: 100%"
              type="primary"
              @click="doLogin(formRef)"
              >登录
            </el-button>
          </div>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import axios from "@/utils/http.js";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { saveToken } from "@/utils/auth.js";

const formRef = ref();
const router = useRouter();

const loginData = reactive({
  userName: "",
  password: "",
});

function forgetPwd() {
  ElMessage({
    showClose: true,
    message: "Oops, 还没有开发完成.",
    grouping: true,
    type: "error",
  });
}

function doLogin(fromEl) {
  if (!fromEl) return;
  fromEl.validate(async (valid) => {
    if (valid) {
      let res = await axios.post("/kharazim-api/auth/login", loginData);
      let { success, data } = res.data;
      if (success) {
        saveToken(data);
        await router.push("/dashboard");
      }
    }
  });
}
</script>

<style scoped>
.login-block {
  display: flex;
  justify-content: center;
  text-align: center;
}

.login-content {
  width: 500px;
  align-content: center;
  justify-content: center;
}
</style>
