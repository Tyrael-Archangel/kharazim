package com.tyrael.kharazim.purchase.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 采购单商品明细
 *
 * @author Tyrael Archangel
 * @since 2024/5/31
 */
@Data
public class PurchaseOrderItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 采购单号
     */
    private String purchaseOrderCode;

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

    /**
     * 单价（元）
     */
    private BigDecimal price;

    /**
     * 商品项金额（元）
     */
    private BigDecimal amount;

    public int getRemainQuantity() {
        return quantity - receivedQuantity;
    }

    public void increaseReceive(Integer quantity) {
        this.receivedQuantity += quantity;
    }

}
