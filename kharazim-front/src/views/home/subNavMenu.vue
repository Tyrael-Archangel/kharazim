<template>
  <template v-for="menuItem in filterNav(menuData)">
    <el-sub-menu
      v-if="menuItem.children && filterNav(menuItem.children).length > 0"
      :key="menuItem.id"
      :index="menuItem.path"
    >
      <template #title>
        <el-image :src="menuItem.icon" class="menu-icon" />
        <span style="font-size: medium">{{ menuItem.name }}</span>
      </template>
      <SubMenu :menu-data="filterNav(menuItem.children)"></SubMenu>
    </el-sub-menu>
    <el-menu-item v-else :key="menuItem.path" :index="menuItem.path">
      <template #title>
        <el-image :src="menuItem.icon" class="menu-icon" />
        <span style="font-size: medium">{{ menuItem.name }}</span>
      </template>
    </el-menu-item>
  </template>
</template>

<script>
export default {
  name: "SubMenu",
  props: ["menuData"],
  methods: {
    filterNav: (menuData) => {
      if (menuData) {
        return menuData.filter((x) => !x.hiddenMenu);
      }
      return menuData;
    },
  },
};
</script>

<style scoped>
.menu-icon {
  display: flex;
  overflow: hidden;
  width: 20px;
  padding-right: 5px;
  fill: red;
}

/* 设置选中菜单项的背景色 */
.el-menu-item.is-active {
  background-color: #ee7e32 !important;
  color: white;
}
</style>
