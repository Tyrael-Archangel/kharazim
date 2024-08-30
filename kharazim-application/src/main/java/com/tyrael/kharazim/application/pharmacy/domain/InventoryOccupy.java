package com.tyrael.kharazim.application.pharmacy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/8/30
 */
@Data
public class InventoryOccupy {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 业务单据编码
     */
    private String businessCode;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * 诊所编码
     */
    private String clinicCode;

    /**
     * 数量
     */
    private Integer quantity;

}
