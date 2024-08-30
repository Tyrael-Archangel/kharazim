package com.tyrael.kharazim.application.pharmacy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.application.pharmacy.enums.InventoryChangeTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/8/9
 */
@Data
public class InventoryLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联业务编码
     */
    private String businessCode;

    /**
     * 批次序列号
     */
    private String serialCode;

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * 数量，非负数
     */
    private Integer quantity;

    /**
     * 结存数量
     */
    private Integer balanceQuantity;

    /**
     * 结存预占数量
     */
    private Integer balanceOccupyQuantity;

    /**
     * 诊所编码
     */
    private String clinicCode;

    /**
     * 库存变化类型
     */
    private InventoryChangeTypeEnum changeType;

    private LocalDateTime operateTime;
    private String operator;
    private String operatorCode;

    public void setQuantity(Integer quantity) {
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be less than 0");
        }
        this.quantity = quantity;
    }

}
