package com.tyrael.kharazim.application.skupublish.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.application.base.BaseDO;
import com.tyrael.kharazim.application.skupublish.enums.SkuPublishStatus;
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
     * 是否已取消
     */
    private Boolean canceled;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * 诊所
     */
    private String clinicCode;

    /**
     * 单价（元）
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

    public SkuPublishStatus getStatus() {
        if (Boolean.TRUE.equals(canceled)) {
            return SkuPublishStatus.CANCELED;
        }
        LocalDateTime now = LocalDateTime.now();
        if (effectBegin.isAfter(now)) {
            return SkuPublishStatus.WAIT_EFFECT;
        }
        if (effectEnd.isBefore(now)) {
            return SkuPublishStatus.LOST_EFFECT;
        }
        return SkuPublishStatus.IN_EFFECT;
    }

}
