package com.tyrael.kharazim.application.pharmacy.vo.inventory;

import com.tyrael.kharazim.application.pharmacy.enums.InventoryOutInTypeEnum;

import java.util.List;

/**
 * 库存变更命令
 *
 * @param businessCode 业务单据编码
 * @param clinicCode   诊所编码
 * @param outInType    出入库类型
 * @param items        出入库明细
 * @param operator     操作人
 * @param operatorCode 操作人编码
 */
public record InventoryChangeCommand(String businessCode,
                                     String clinicCode,
                                     InventoryOutInTypeEnum outInType,
                                     List<Item> items,
                                     String operator,
                                     String operatorCode) {
    /**
     * 库存
     *
     * @param skuCode  SKU编码
     * @param quantity 数量
     */
    public record Item(String skuCode, Integer quantity) {
    }
}
