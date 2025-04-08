package com.tyrael.kharazim.finance.app.vo.settlement;

import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.finance.app.constant.FinanceDictConstants;
import com.tyrael.kharazim.finance.app.enums.SettlementOrderStatus;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

    @Schema(description = "来源处方编码")
    private String sourcePrescriptionCode;

    @ArraySchema(arraySchema = @Schema(description = "机构（诊所）编码"))
    private Set<String> clinicCodes;

    @Schema(description = "会员编码")
    private String customerCode;

    /**
     * {@link FinanceDictConstants#SETTLEMENT_ORDER_STATUS}
     */
    @Schema(description = "结算状态，字典编码: settlement_order_status", implementation = SettlementOrderStatus.class)
    private SettlementOrderStatus status;

}
