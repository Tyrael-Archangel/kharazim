package com.tyrael.kharazim.application.pharmacy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.application.pharmacy.enums.InventoryOutInTypeEnum;
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
    private String sourceBusinessCode;

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * 数量，非负数
     */
    private Integer quantity;

    /**
     * 诊所编码
     */
    private String clinicCode;

    /**
     * 出入库类型
     */
    private InventoryOutInTypeEnum outInType;

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
