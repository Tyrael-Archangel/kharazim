package com.tyrael.kharazim.application.settlement.vo;

import com.tyrael.kharazim.application.settlement.enums.SettlementOrderStatus;
import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2024/4/3
 */
@Data
public class PageSettlementOrderRequest extends PageCommand {

    @Schema(description = "结算单编码")
    private String settlementOrderCode;

    @Schema(description = "机构（诊所）编码")
    private Set<String> clinicCodes;

    @Schema(description = "会员编码")
    private String customerCode;

    @Schema(description = "结算状态")
    private SettlementOrderStatus status;

}
