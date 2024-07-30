import { ConfigEnv, defineConfig, loadEnv, UserConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import * as path from "path";

// https://vitejs.dev/config/
export default defineConfig(({ mode }: ConfigEnv): UserConfig => {
  const env = loadEnv(mode, process.cwd());
  return {
    server: {
      open: true,
      port: Number(env.VITE_APP_PORT),
      proxy: {
        "/kharazim-api": env.VITE_APP_SERVICE_API,
        // 反向代理解决跨域
        // "/api": {
        //   target: env.VITE_APP_SERVICE_API,
        //   changeOrigin: true,
        //   rewrite: (path) =>
        //       path.replace(new RegExp("^/api"), ""),
        // },
      },
    },
    plugins: [vue()],
    resolve: {
      // 配置路径别名
      alias: {
        "@": path.resolve(__dirname, "./src"),
      },
    },
  };
});
