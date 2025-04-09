package com.tyrael.kharazim.pharmacy.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
@Data
public class OutboundOrderItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 出库单编码
     */
    private String outboundOrderCode;

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * 数量
     */
    private Integer quantity;

}
