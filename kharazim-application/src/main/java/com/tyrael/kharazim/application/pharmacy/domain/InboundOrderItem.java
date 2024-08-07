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
     * 已收货数量
     */
    private Integer receivedQuantity;

    public int getRemainQuantity() {
        return quantity - receivedQuantity;
    }

    public void addReceivedQuantity(int quantity) {
        // 允许多收货，不校验收货数量是否超额
        this.receivedQuantity += quantity;
    }

}
