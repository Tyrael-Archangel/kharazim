package com.tyrael.kharazim.application.config;

import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@Getter
@SuppressWarnings("unused")
public enum BusinessCodeConstants {

    // --- TEST
    SYSTEM_TEST("测试编码", 5, "TEST"),
    // --- TEST

    USER("用户编码", "U"),
    ROLE("角色（岗位）编码", "R"),
    CUSTOMER("会员编码", 10, "CU"),
    CUSTOMER_RECHARGE_CARD("会员储值单", "CRC"),
    CUSTOMER_WALLET_TRANSACTION("会员交易流水号编码", "TRS"),
    CUSTOMER_FAMILY("会员家庭", "CF"),
    CLINIC("诊所（机构）", "CL"),
    APPOINTMENT_ORDER("预约单", "AO"),
    TRADE_ORDER("销售单", 3, "SO"),
    TRADE_AFTER_SALE("售后单", 3, "RO"),
    RECHARGE_CARD_TYPE("储值卡项", "RCT"),
    SETTLEMENT_ORDER("结算单", "BO"),
    CLIENTELE("客户编码", 10, "KH"),
    WORKER_ORDER("工单", 5, "WO"),
    PRODUCT_UNIT("商品单位编码", 4, "UT"),
    EMR("电子病例", 4, "EMR"),
    PRESCRIPTION("处方", 4, "RX"),
    SUPPLIER("供应商", "SU"),
    PRODUCT_CATEGORY("商品单位", 3, "PC"),
    SKU("SKU", 6),
    SKU_PUBLISH("商品发布", 5, "SPB"),
    FILE("文件", 10, "F"),
    FILE_DIR("文件目录", 5, "dir"),
    PURCHASE_ORDER("采购订单", 3, "PO"),
    INBOUND_ORDER("入库单", 5, "IO"),
    OUTBOUND_ORDER("出库单", 5, "DO"),
    INVENTORY_INBOUND("库存入库流水", "IIN"),
    INVENTORY_OUTBOUND("库存出库流水", "IOT"),
    PURCHASE_ORDER_RECEIVE("采购单收货流水", "POI");

    private static final int DEFAULT_BIT = 6;

    private final String prefix;
    private final String desc;
    private final int bit;

    BusinessCodeConstants(String desc) {
        this(desc, DEFAULT_BIT);
    }

    BusinessCodeConstants(String desc, int bit) {
        this(desc, bit, null);
    }

    BusinessCodeConstants(String desc, String prefix) {
        this(desc, DEFAULT_BIT, prefix);
    }

    BusinessCodeConstants(String desc, int bit, String prefix) {
        this.prefix = prefix;
        this.desc = desc;
        this.bit = bit;
    }
}
