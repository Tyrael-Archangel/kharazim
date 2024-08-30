package com.tyrael.kharazim.application.pharmacy.vo.inventory;

import lombok.Getter;

import java.util.List;

import static com.tyrael.kharazim.application.pharmacy.enums.InventoryChangeTypeEnum.SALE_OUT;

/**
 * 库存变更命令-出库
 *
 * @author Tyrael Archangel
 * @since 2024/8/30
 */
@Getter
public final class InventoryOutboundCommand extends InventoryChangeCommand {

    /**
     * 预占流水号
     */
    private final String occupySerialCode;

    /**
     * @param businessCode     业务单据编码
     * @param serialCode       操作批次流水号
     * @param clinicCode       诊所编码
     * @param items            商品明细
     * @param operator         操作人
     * @param operatorCode     操作人编码
     * @param occupySerialCode 预占流水号
     */
    public InventoryOutboundCommand(String businessCode,
                                    String serialCode,
                                    String clinicCode,
                                    List<Item> items,
                                    String operator,
                                    String operatorCode,
                                    String occupySerialCode) {
        super(businessCode, serialCode, clinicCode, SALE_OUT, items, operator, operatorCode);
        this.occupySerialCode = occupySerialCode;
    }

}
