package com.tyrael.kharazim.application.skupublish.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.application.base.BaseDO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品发布
 *
 * @author Tyrael Archangel
 * @since 2024/3/16
 */
@Data
public class SkuPublish extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品发布序列号
     */
    private String code;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * 诊所
     */
    private String clinicCode;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 生效时间
     */
    private LocalDateTime effectBegin;

    /**
     * 失效时间
     */
    private LocalDateTime effectEnd;

}
