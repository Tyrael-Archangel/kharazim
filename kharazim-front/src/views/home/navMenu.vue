<template>
  <aside :style="'background-color:' + navBackgroundColor" class="nav-aside">
    <el-menu :background-color="navBackgroundColor" :default-active="currentActiveMenu" :unique-opened="true" router>
      <SubNavMenu :menu-data="navs" />
    </el-menu>
  </aside>
</template>

<script lang="ts" setup>
import SubNavMenu from "./subNavMenu.vue";
import { navs } from "@/router/navData.ts";
import { useRoute } from "vue-router";
import { onMounted, ref } from "vue";
import axios from "@/utils/http.js";
import { AxiosResponse } from "axios";

const navBackgroundColors = ref({
  default: "#ffffff",
  dev: "#e1f3d8",
  test: "#8bbdf8",
  beta: "#e1aad2",
  prod: "#ffffff",
});

const navBackgroundColor = ref<string>(navBackgroundColors.value.default);

const route = useRoute();

const currentActiveMenu = ref<string>();
onMounted(() => {
  currentActiveMenu.value = route.path;
  loadProfile();
});

function loadProfile() {
  axios.get("/kharazim-api/").then((res: AxiosResponse) => {
    const profile = res.data.profile as keyof typeof navBackgroundColors.value;
    const color = navBackgroundColors.value[profile];
    if (color) {
      navBackgroundColor.value = color;
    }
  });
}
</script>
<style scoped>
.nav-aside {
  flex-shrink: 0;
  overflow-y: auto;
  scrollbar-width: none;
  width: 190px;
}
</style>
