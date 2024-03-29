package com.tyrael.kharazim.application.product.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tyrael.kharazim.application.base.BaseDO;
import com.tyrael.kharazim.application.product.typehandler.ListAttributeTypeHandler;
import com.tyrael.kharazim.application.product.typehandler.ListStringTypeHandler;
import com.tyrael.kharazim.application.product.vo.sku.Attribute;
import lombok.Data;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/3/1
 */
@Data
@TableName(autoResultMap = true)
public class ProductSku extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * sku编码
     */
    private String code;

    /**
     * sku名称
     */
    private String name;

    /**
     * 商品分类编码
     */
    private String categoryCode;

    /**
     * 供应商编码
     */
    private String supplierCode;

    /**
     * 单位编码
     */
    private String unitCode;

    /**
     * 默认图片
     */
    private String defaultImage;

    /**
     * 图片
     */
    @TableField(typeHandler = ListStringTypeHandler.class)
    private List<String> images;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 属性信息
     */
    @TableField(typeHandler = ListAttributeTypeHandler.class)
    private List<Attribute> attributes;

}
