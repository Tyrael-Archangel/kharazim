package com.tyrael.kharazim.pharmacy.app.vo.inbound;

import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.pharmacy.app.constant.PharmacyDictConstants;
import com.tyrael.kharazim.pharmacy.app.enums.InboundOrderStatus;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2024/8/12
 */
@Data
public class PageInboundOrderRequest extends PageCommand {

    @Schema(description = "入库单号")
    private String code;

    @Schema(description = "来源单据编码")
    private String sourceBusinessCode;

    @ArraySchema(arraySchema = @Schema(description = "诊所编码"))
    private Set<String> clinicCodes;

    @ArraySchema(arraySchema = @Schema(description = "供应商编码"))
    private Set<String> supplierCodes;

    /**
     * {@link PharmacyDictConstants#INBOUND_ORDER_STATUS}
     */
    @Schema(description = "入库单状态，字典编码: inbound_order_status", implementation = InboundOrderStatus.class)
    private InboundOrderStatus status;

}
