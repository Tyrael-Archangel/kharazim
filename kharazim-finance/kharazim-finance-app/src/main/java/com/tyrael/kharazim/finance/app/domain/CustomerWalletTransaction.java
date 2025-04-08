package com.tyrael.kharazim.finance.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.finance.app.enums.TransactionSourceEnum;
import com.tyrael.kharazim.finance.app.enums.TransactionTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员交易流水
 * @author Tyrael Archangel
 * @since 2024/2/5
 */
@Data
public class CustomerWalletTransaction {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 交易流水号
     */
    private String code;

    /**
     * 会员编码
     */
    private String customerCode;

    /**
     * 交易类型，充值、消费、退款
     */
    private TransactionTypeEnum type;

    /**
     * 关联的业务来源类型
     */
    private TransactionSourceEnum source;

    /**
     * 关联的业务单号
     */
    private String sourceBusinessCode;

    /**
     * 交易时间
     */
    private LocalDateTime transactionTime;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 操作人
     */
    private String operator;
    private String operatorCode;

    private String remark;

}
