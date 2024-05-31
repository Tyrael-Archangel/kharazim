package com.tyrael.kharazim.application.purchase.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/5/31
 */
@Data
public class PurchaseOrderReceiveRecordItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 收货记录流水号
     */
    private String receiveSerialCode;

    /**
     * 收货商品
     */
    private String skuCode;

    /**
     * 收货数量
     */
    private Integer quantity;

}
