interface Nav {
  id: number;
  name: string;
  component: string | null; // 目录为null，菜单不能为null
  path: string; // 路径
  icon: string | null;
  children: Nav[] | null;
}

let idValue: number = 1;

export const navs: Nav[] = [
  {
    id: idValue++,
    name: "首页",
    component: "dashboard/index.vue",
    path: "/dashboard",
    icon: "/icons/home.svg",
    children: null,
  },
  {
    id: idValue++,
    name: "系统管理",
    component: null,
    path: "/system-setting",
    icon: "/icons/setting.svg",
    children: [
      {
        id: idValue++,
        name: "用户管理",
        component: "userManagement/index.vue",
        path: "/system-setting/user-management",
        icon: "/icons/user.svg",
        children: null,
      },
      {
        id: idValue++,
        name: "角色管理",
        component: "roleManagement/index.vue",
        path: "/system-setting/role-management",
        icon: "/icons/role.svg",
        children: null,
      },
    ],
  },
  {
    id: idValue++,
    name: "会员档案",
    component: null,
    path: "/customer-info",
    icon: "/icons/customer_management.svg",
    children: [
      {
        id: idValue++,
        name: "会员管理",
        component: "customer/index.vue",
        path: "/customer-info",
        icon: "/icons/customer.svg",
        children: null,
      },
      {
        id: idValue++,
        name: "家庭管理",
        component: "customerFamily/index.vue",
        path: "/customer-family",
        icon: "/icons/family.svg",
        children: null,
      },
    ],
  },
  {
    id: idValue++,
    name: "诊所管理",
    component: null,
    path: "/clinic-management",
    icon: "/icons/clinic_management.svg",
    children: [
      {
        id: idValue++,
        name: "诊所列表",
        component: "clinic/clinicManagement.vue",
        path: "/clinic-info",
        icon: "/icons/clinic.svg",
        children: null,
      },
    ],
  },
  {
    id: idValue++,
    name: "供应商管理",
    component: null,
    path: "/supplier-management",
    icon: "/icons/supplier_management.svg",
    children: [
      {
        id: idValue++,
        name: "供应商列表",
        component: "supplier/index.vue",
        path: "/supplier-info",
        icon: "/icons/supplier.svg",
        children: null,
      },
    ],
  },
  {
    id: idValue++,
    name: "商品中心",
    component: null,
    path: "/product-management",
    icon: "/icons/product_management.svg",
    children: [
      {
        id: idValue++,
        name: "商品列表",
        component: "product/productInfo.vue",
        path: "/product-info",
        icon: "/icons/product_info.svg",
        children: null,
      },
      {
        id: idValue++,
        name: "商品发布",
        component: "product/productPublish.vue",
        path: "/product-publish",
        icon: "/icons/product_publish.svg",
        children: null,
      },
      {
        id: idValue++,
        name: "商品分类",
        component: "product/productCategory.vue",
        path: "/product-category",
        icon: "/icons/product_category.svg",
        children: null,
      },
      {
        id: idValue++,
        name: "商品单位",
        component: "product/productUnit.vue",
        path: "/product-unit",
        icon: "/icons/product_unit.svg",
        children: null,
      },
    ],
  },
  {
    id: idValue++,
    name: "Demo",
    component: "demo/index.vue",
    path: "/demo",
    icon: "/icons/demo.svg",
    children: null,
  },
];
