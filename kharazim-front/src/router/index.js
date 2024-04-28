import { createRouter, createWebHashHistory } from "vue-router";
import { navs } from "@/router/navData.ts";

const modules = import.meta.glob("/src/views/**/**.vue");

let homeRoutes = [];
buildHomeRoutes(navs, [], homeRoutes);

function buildHomeRoutes(menus, parentFullPath, homeRoutes) {
  if (menus && menus.length !== 0) {
    menus.forEach((menu) => {
      if (menu.component) {
        homeRoutes.push({
          path: menu.path,
          name: menu.name,
          meta: {
            id: menu.id,
            fullPath: [...parentFullPath, menu.name],
            name: menu.name,
          },
          component: modules[`/src/views/${menu.component}`],
        });
      }
      buildHomeRoutes(
        menu.children,
        [...parentFullPath, menu.name],
        homeRoutes,
      );
    });
  }
}

const routes = [
  {
    path: "/",
    component: () => import("@/views/index.vue"),
  },
  {
    path: "/home",
    component: () => import("@/views/home/index.vue"),
    props: {
      default: true,
    },
    children: homeRoutes,
  },
  {
    path: "/login",
    component: () => import("@/views/login/index.vue"),
  },
];

export const router = createRouter({
  history: createWebHashHistory(),
  routes,
});
