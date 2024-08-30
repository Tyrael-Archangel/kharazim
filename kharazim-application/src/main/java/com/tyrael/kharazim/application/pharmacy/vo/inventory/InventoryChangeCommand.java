package com.tyrael.kharazim.application.pharmacy.vo.inventory;

import com.tyrael.kharazim.application.pharmacy.enums.InventoryChangeTypeEnum;
import lombok.Getter;

import java.util.List;

/**
 * 库存变更命令
 *
 * @author Tyrael Archangel
 * @since 2024/8/30
 */
@Getter
public abstract sealed class InventoryChangeCommand permits
        InventoryInboundCommand, InventoryOccupyCommand, InventoryOutboundCommand {

    /**
     * 业务单据编码
     */
    private final String businessCode;
    /**
     * 批次流水号
     */
    private final String serialCode;
    /**
     * 诊所编码
     */
    private final String clinicCode;
    /**
     * 库存变更类型
     */
    private final InventoryChangeTypeEnum changeType;
    /**
     * 商品明细
     */
    private final List<Item> items;
    /**
     * 操作人
     */
    private final String operator;
    /**
     * 操作人编码
     */
    private final String operatorCode;

    protected InventoryChangeCommand(String businessCode,
                                     String serialCode,
                                     String clinicCode,
                                     InventoryChangeTypeEnum changeType,
                                     List<Item> items,
                                     String operator,
                                     String operatorCode) {
        this.businessCode = businessCode;
        this.serialCode = serialCode;
        this.clinicCode = clinicCode;
        this.changeType = changeType;
        this.items = items;
        this.operator = operator;
        this.operatorCode = operatorCode;
    }

    /**
     * 库存
     *
     * @param skuCode  SKU编码
     * @param quantity 数量
     */
    public record Item(String skuCode, Integer quantity) {
    }
}
