package com.tyrael.kharazim.application.recharge.vo;

import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author Tyrael Archangel
 * @since 2024/2/4
 */
@Data
public class CustomerRechargeCardPageRequest extends PageCommand {

    @Schema(description = "起始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "储值单号")
    private String code;

    @Schema(description = "会员编码")
    private String customerCode;

    @Schema(description = "成交员工编码")
    private String traderUserCode;

}
