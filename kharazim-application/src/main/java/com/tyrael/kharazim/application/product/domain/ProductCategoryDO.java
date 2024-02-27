package com.tyrael.kharazim.application.product.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tyrael.kharazim.application.base.BaseDO;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/2/27
 */
@Data
@TableName(value = "product_category", autoResultMap = true)
public class ProductCategoryDO extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父级分类ID
     */
    private Long parentId;

    /**
     * 分类编码
     */
    private String code;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

}
