package com.tyrael.kharazim.application.recharge.vo;

import com.tyrael.kharazim.application.recharge.enums.CustomerRechargeCardLogType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/2/5
 */
@Data
@Builder
public class CustomerRechargeCardLogVO {

    private Long id;

    @Schema(description = "储值单号")
    private String rechargeCardCode;

    @Schema(description = "会员编码")
    private String customerCode;

    @Schema(description = "会员姓名")
    private String customerName;

    @Schema(description = "日志类型")
    private CustomerRechargeCardLogType logType;

    @Schema(description = "关联的业务单号，例如结算单号")
    private String sourceBusinessCode;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "操作人")
    private String operator;

    @Schema(description = "操作人编码")
    private String operatorCode;

    @Schema(description = "备注")
    private String remark;

}
