package com.tyrael.kharazim.finance.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.finance.app.enums.CustomerRechargeCardLogType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/2/5
 */
@Data
public class CustomerRechargeCardLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 储值单号
     */
    private String rechargeCardCode;

    /**
     * 会员编码
     */
    private String customerCode;

    /**
     * 日志类型
     */
    private CustomerRechargeCardLogType logType;

    /**
     * 关联的业务单号，例如结算单号
     */
    private String sourceBusinessCode;

    /**
     * 时间
     */
    private LocalDateTime createTime;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 操作人
     */
    private String operator;
    private String operatorCode;

    private String remark;

}
