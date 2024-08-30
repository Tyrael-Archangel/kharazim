package com.tyrael.kharazim.application.pharmacy.vo.inventory;

import java.util.List;

import static com.tyrael.kharazim.application.pharmacy.enums.InventoryChangeTypeEnum.PURCHASE_IN;

/**
 * 库存变更命令-入库
 *
 * @author Tyrael Archangel
 * @since 2024/8/30
 */
public final class InventoryInboundCommand extends InventoryChangeCommand {

    /**
     * @param businessCode 业务单据编码
     * @param serialCode   操作批次流水号
     * @param clinicCode   诊所编码
     * @param items        商品明细
     * @param operator     操作人
     * @param operatorCode 操作人编码
     */
    public InventoryInboundCommand(String businessCode,
                                   String serialCode,
                                   String clinicCode,
                                   List<Item> items,
                                   String operator,
                                   String operatorCode) {
        super(businessCode, serialCode, clinicCode, PURCHASE_IN, items, operator, operatorCode);
    }

}
