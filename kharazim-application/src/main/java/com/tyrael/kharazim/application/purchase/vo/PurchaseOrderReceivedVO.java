package com.tyrael.kharazim.application.purchase.vo;

import java.util.List;

/**
 * 采购单收货数据
 *
 * @param purchaseOrderCode 采购单号
 * @param serialCode        流水号
 * @param items             明细数据
 * @param operator          操作人
 * @param operatorCode      操作人编码
 * @author Tyrael Archangel
 * @since 2024/8/9
 */
public record PurchaseOrderReceivedVO(String purchaseOrderCode,
                                      String serialCode,
                                      List<Item> items,
                                      String operator,
                                      String operatorCode) {

    /**
     * 明细
     *
     * @param skuCode  SKU编码
     * @param quantity 数量
     */
    public record Item(String skuCode, Integer quantity) {
    }
}
