import { createApp } from "vue";
import { createPinia } from "pinia";
import "./style.css";
import "element-plus/dist/index.css";
import App from "./App.vue";

import { router } from "./router/index.js";
import ElementPlus from "element-plus";
import zhLocale from "element-plus/es/locale/lang/zh-cn";

import * as ElementPlusIconsVue from "@element-plus/icons-vue";

const app = createApp(App);
app.use(createPinia());
app.use(ElementPlus, { locale: zhLocale });
app.use(router);
app.mount("#app");
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}
