package com.tyrael.kharazim.application.pharmacy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/7/12
 */
@Data
public class InboundOrderItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 入库单编码
     */
    private String inboundOrderCode;

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 已入库数量
     */
    private Integer inboundedQuantity;

    public int getRemainQuantity() {
        return quantity - inboundedQuantity;
    }

    public void addInboundQuantity(int quantity) {
        // 允许多入库，不校验入库数量是否超额
        this.inboundedQuantity += quantity;
    }

}
