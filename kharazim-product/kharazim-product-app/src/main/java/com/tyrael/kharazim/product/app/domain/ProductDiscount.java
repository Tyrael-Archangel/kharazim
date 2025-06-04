package com.tyrael.kharazim.product.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tyrael.kharazim.mybatis.BaseDO;
import com.tyrael.kharazim.product.app.enums.DiscountType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2025/5/26
 */
@Data
@TableName(autoResultMap = true)
public class ProductDiscount extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String clinicCode;

    private LocalDateTime effectBegin;

    private LocalDateTime effectEnd;

    private BigDecimal value;

    private DiscountType valueType;

    private Boolean enabled;

}
