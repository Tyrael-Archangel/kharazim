package com.tyrael.kharazim.application.pharmacy.vo.outboundorder;

import com.tyrael.kharazim.application.pharmacy.enums.OutboundOrderStatus;
import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
@Data
public class PageOutboundOrderRequest extends PageCommand {

    @Schema(description = "出库单编码")
    private String code;

    @Schema(description = "来源单据编码")
    private String sourceBusinessCode;

    @Schema(description = "出库单状态", implementation = OutboundOrderStatus.class)
    private OutboundOrderStatus status;

    @ArraySchema(arraySchema = @Schema(description = "机构（诊所）编码"))
    private Set<String> clinicCodes;

    @Schema(description = "会员编码")
    private String customerCode;

}
