package com.tyrael.kharazim.application.product.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.application.base.BaseDO;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/3/1
 */
@Data
public class ProductSpu extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * spu编码
     */
    private String code;

    /**
     * spu名称
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
     * 默认图片
     */
    private String defaultImage;

    /**
     * 描述信息
     */
    private String description;

}
