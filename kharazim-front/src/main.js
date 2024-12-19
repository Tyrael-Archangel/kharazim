import { createApp } from "vue";
import { createPinia } from "pinia";
import "./style.css";
import "element-plus/dist/index.css";
import App from "./App.vue";

import { router } from "./router/index.js";
import ElementPlus from "element-plus";
import zhLocale from "element-plus/es/locale/lang/zh-cn";

import * as ElementPlusIconsVue from "@element-plus/icons-vue";

// 引入 echarts 核心模块，核心模块提供了 echarts 使用必须要的接口。
import * as echarts from "echarts/core";
// 引入柱状图图表，图表后缀都为 Chart
import { BarChart } from "echarts/charts";
// 引入提示框，标题，直角坐标系，数据集，内置数据转换器组件，组件后缀都为 Component
import {
  DatasetComponent,
  GridComponent,
  TitleComponent,
  TooltipComponent,
  TransformComponent,
} from "echarts/components"; // 标签自动布局、全局过渡动画等特性
// 引入 Canvas 渲染器，注意引入 CanvasRenderer 或者 SVGRenderer 是必须的一步
import { LabelLayout, UniversalTransition } from "echarts/features";
import { CanvasRenderer } from "echarts/renderers";

// 注册必须的组件
echarts.use([
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DatasetComponent,
  TransformComponent,
  BarChart,
  LabelLayout,
  UniversalTransition,
  CanvasRenderer,
]);

const app = createApp(App);
app.use(createPinia());
app.use(ElementPlus, { locale: zhLocale });
app.use(router);
app.mount("#app");
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}
