package com.tyrael.kharazim.pharmacy.app.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Data
public class Inventory {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 诊所编码
     */
    private String clinicCode;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * 在库库存数量
     */
    private Integer quantity;

    /**
     * 已预占数量
     */
    private Integer occupiedQuantity;

    /**
     * 可用库存数量，数据库虚拟字段
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private Integer usableQuantity;

}
