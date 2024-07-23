interface Nav {
  id?: number;
  name: string;
  component?: string | null; // 目录为null，菜单不能为null
  path: string; // 路径
  icon: string | null;
  hiddenMenu?: boolean;
  children?: Nav[] | null;
}

const navs: Nav[] = [
  {
    name: "首页",
    component: "dashboard/index.vue",
    path: "/dashboard",
    icon: "/icons/home.svg",
  },
  {
    name: "系统管理",
    path: "/system-setting",
    icon: "/icons/setting.svg",
    children: [
      {
        name: "用户管理",
        component: "userManagement/index.vue",
        path: "/system-setting/user-management",
        icon: "/icons/user.svg",
      },
      {
        name: "角色管理",
        component: "roleManagement/index.vue",
        path: "/system-setting/role-management",
        icon: "/icons/role.svg",
      },
    ],
  },
  {
    name: "会员档案",
    path: "/customer-info",
    icon: "/icons/customer_management.svg",
    children: [
      {
        name: "会员管理",
        component: "customer/index.vue",
        path: "/customer-info",
        icon: "/icons/customer.svg",
      },
      {
        name: "家庭管理",
        component: "customerFamily/index.vue",
        path: "/customer-family",
        icon: "/icons/family.svg",
      },
    ],
  },
  {
    name: "诊所管理",
    path: "/clinic-management",
    icon: "/icons/clinic_management.svg",
    children: [
      {
        name: "诊所列表",
        component: "clinic/clinicManagement.vue",
        path: "/clinic-info",
        icon: "/icons/clinic.svg",
      },
    ],
  },
  {
    name: "采购管理",
    path: "/purchase-management",
    icon: "/icons/purchase_management.svg",
    children: [
      {
        name: "供应商管理",
        component: "supplier/supplier.vue",
        path: "/supplier-info",
        icon: "/icons/supplier.svg",
      },{
        name: "采购单管理",
        component: "purchaseOrder/purchaseOrder.vue",
        path: "/purchase-order",
        icon: "/icons/purchase_order.svg",
      }
    ],
  },
  {
    name: "诊疗中心",
    path: "/diagnosis-treat",
    icon: "/icons/diagnosis_treat.svg",
    children: [
      {
        name: "处方管理",
        component: "prescription/prescriptionInfo.vue",
        path: "/prescription-info",
        icon: "/icons/prescription.svg",
        children: [
          {
            name: "创建处方",
            component: "prescription/createPrescription.vue",
            path: "/create-prescription",
            icon: "/icons/prescription.svg",
            hiddenMenu: true,
          },
        ],
      },
    ],
  },
  {
    name: "结算中心",
    path: "/settlement-center",
    icon: "/icons/settlement_center.svg",
    children: [
      {
        name: "结算单管理",
        component: "settlementOrder/settlementOrder.vue",
        path: "/settlement-order",
        icon: "/icons/settlement_order.svg",
      },
      {
        name: "储值卡项",
        component: "rechargeCard/rechargeCardType.vue",
        path: "/recharge-card-type",
        icon: "/icons/recharge_card_type.svg",
      },
      {
        name: "会员储值单",
        component: "rechargeCard/customerRechargeCard.vue",
        path: "/customer-recharge-card",
        icon: "/icons/customer_recharge_card.svg",
      },
    ],
  },
  {
    name: "商品中心",
    path: "/product-management",
    icon: "/icons/product_management.svg",
    children: [
      {
        name: "商品列表",
        component: "product/productInfo.vue",
        path: "/product-info",
        icon: "/icons/product_info.svg",
      },
      {
        name: "商品发布",
        component: "product/productPublish.vue",
        path: "/product-publish",
        icon: "/icons/product_publish.svg",
      },
      {
        name: "商品分类",
        component: "product/productCategory.vue",
        path: "/product-category",
        icon: "/icons/product_category.svg",
      },
      {
        name: "商品单位",
        component: "product/productUnit.vue",
        path: "/product-unit",
        icon: "/icons/product_unit.svg",
      },
    ],
  },
  {
    name: "Demo",
    component: "demo/index.vue",
    path: "/demo",
    icon: "/icons/demo.svg",
  },
];

let idValue: number = 1;

function generateId(navs: Nav[] | null | undefined) {
  if (navs) {
    for (let nav of navs) {
      nav.id = idValue++;
      generateId(nav.children);
    }
  }
}

generateId(navs);

export { navs };
