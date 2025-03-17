package com.tyrael.kharazim.basicdata.app.domain.recharge;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.mybatis.BaseDO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2024/1/25
 */
@Data
public class RechargeCardType extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 储值卡项名称
     */
    private String name;

    /**
     * 折扣百分比
     */
    private BigDecimal discountPercent;

    /**
     * 是否永不过期
     */
    private Boolean neverExpire;

    /**
     * 有效期天数
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Integer validPeriodDays;

    /**
     * 默认卡金额
     */
    private BigDecimal defaultAmount;

    /**
     * 是否可以创建新卡
     */
    private Boolean canCreateNewCard;

    public boolean enabled() {
        return Boolean.TRUE.equals(canCreateNewCard);
    }

    public boolean forbidden() {
        return !enabled();
    }

}