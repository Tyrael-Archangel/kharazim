package com.tyrael.kharazim.product.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tyrael.kharazim.mybatis.BaseDO;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/2/20
 */
@Data
@TableName(value = "product_unit", autoResultMap = true)
public class ProductUnitDO extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 单位编码
     */
    private String code;

    /**
     * 单位名称
     */
    private String name;

    /**
     * 单位英文名称
     */
    private String englishName;

}