import axios from "axios";
import { ElMessage } from "element-plus";
import { getToken } from "@/utils/auth.js";

const service = axios.create();

// 请求拦截器
service.interceptors.request.use((config) => {
  let accessToken = getToken();
  if (accessToken) {
    config.headers["ACCESS-TOKEN"] = accessToken;
  }
  return config;
});

// 响应拦截器
service.interceptors.response.use(
  (res) => {
    return res;
  },
  (error) => {
    console.log(error);
    const { msg, code } = error.response.data;
    if (msg) {
      ElMessage.error(msg);
    }
    if (code === 401) {
      window.location.href = "/";
    }
    return Promise.reject(error);
  },
);

export default service;