package com.tyrael.kharazim.pharmacy.app.vo.inventory;

import java.util.List;

import static com.tyrael.kharazim.pharmacy.app.enums.InventoryChangeTypeEnum.SALE_OCCUPY;

/**
 * 库存变更命令-预占
 *
 * @author Tyrael Archangel
 * @since 2024/8/30
 */
public final class InventoryOccupyCommand extends InventoryChangeCommand {

    /**
     * @param businessCode 业务单据编码
     * @param clinicCode   诊所编码
     * @param items        商品明细
     * @param operator     操作人
     * @param operatorCode 操作人编码
     */
    public InventoryOccupyCommand(String businessCode,
                                  String clinicCode,
                                  List<Item> items,
                                  String operator,
                                  String operatorCode) {
        super(businessCode, businessCode, clinicCode, SALE_OCCUPY, items, operator, operatorCode);
    }

}
