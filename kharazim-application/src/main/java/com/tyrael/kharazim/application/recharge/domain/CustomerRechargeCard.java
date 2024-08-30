package com.tyrael.kharazim.application.recharge.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.application.base.BaseDO;
import com.tyrael.kharazim.application.recharge.enums.CustomerRechargeCardStatus;
import com.tyrael.kharazim.common.exception.BusinessException;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * @author Tyrael Archangel
 * @since 2024/2/1
 */
@Data
public class CustomerRechargeCard extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 储值单号
     */
    private String code;

    /**
     * 支付状态：未支付，已支付，未退款，已退款
     */
    private CustomerRechargeCardStatus status;

    /**
     * 会员编码
     */
    private String customerCode;

    /**
     * 储值卡项编码
     */
    private String cardTypeCode;

    /**
     * 储值总金额
     */
    private BigDecimal totalAmount;

    /**
     * 已消费金额
     */
    private BigDecimal consumedAmount;

    /**
     * 已消耗的商品原价金额
     */
    private BigDecimal consumedOriginalAmount;

    /**
     * 成交员工编码
     */
    private String traderUserCode;

    /**
     * 折扣百分比
     */
    private BigDecimal discountPercent;

    /**
     * 是否永不过期
     */
    private Boolean neverExpire;

    /**
     * 过期时间
     */
    private LocalDate expireDate;

    /**
     * 储值日期
     */
    private LocalDate rechargeDate;

    /**
     * 退卡金额
     */
    private BigDecimal chargebackAmount;

    /**
     * 退卡员工
     */
    private String chargebackUserCode;

    /**
     * 剩余金额
     */
    public BigDecimal getBalanceAmount() {
        return totalAmount.subtract(consumedAmount);
    }

    /**
     * 是否有效
     */
    public boolean effective() {
        if (!CustomerRechargeCardStatus.PAID.equals(status)) {
            // 未支付，或者已退卡了，无效
            return false;
        }
        if (getBalanceAmount().compareTo(BigDecimal.ZERO) <= 0) {
            // 剩余金额为0，无效
            return false;
        }

        if (Boolean.TRUE.equals(neverExpire)) {
            return true;
        } else {
            return !expireDate.isBefore(LocalDate.now());
        }
    }

    /**
     * 是否永不过期
     */
    public boolean neverExpire() {
        return Boolean.TRUE.equals(neverExpire);
    }

    /**
     * 消费
     *
     * @param useAmount    使用金额
     * @param deductAmount 抵扣金额
     */
    public void consume(BigDecimal useAmount, BigDecimal deductAmount) {
        BusinessException.assertTrue(this.effective(), "储值单[" + this.code + "]已失效");

        BigDecimal discountRate = discountPercent.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
        // 必须满足 (抵扣金额 * 折扣率 == 使用金额) 或者 (使用金额 / 折扣率 == 抵扣金额)
        if (deductAmount.compareTo(useAmount.divide(discountRate, 2, RoundingMode.HALF_UP)) != 0
                && useAmount.compareTo(deductAmount.multiply(discountRate).setScale(2, RoundingMode.HALF_UP)) != 0) {
            throw new BusinessException("储值单[" + this.code + "]抵扣金额与使用金额不匹配");
        }
        if (useAmount.compareTo(this.getBalanceAmount()) > 0) {
            throw new BusinessException("储值单[" + this.code + "]余额不足");
        }
        this.consumedAmount = this.consumedAmount.add(useAmount);
        this.consumedOriginalAmount = this.consumedOriginalAmount.add(deductAmount);
    }

}
