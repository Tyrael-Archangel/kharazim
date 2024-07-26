package com.tyrael.kharazim.application.purchase.vo.request;

import com.tyrael.kharazim.application.purchase.enums.PurchaseOrderPaymentStatus;
import com.tyrael.kharazim.application.purchase.enums.PurchaseOrderReceiveStatus;
import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2024/7/26
 */
@Data
public class PagePurchaseOrderRequest extends PageCommand {

    @Schema(description = "采购单编码")
    private String purchaseOrderCode;

    @ArraySchema(arraySchema = @Schema(description = "诊所（机构）编码"))
    private Set<String> clinicCodes;

    @ArraySchema(arraySchema = @Schema(description = "供应商编码"))
    private Set<String> supplierCodes;

    @ArraySchema(arraySchema = @Schema(description = "收货状态", implementation = PurchaseOrderReceiveStatus.class))
    private Set<PurchaseOrderReceiveStatus> receiveStatuses;

    @ArraySchema(arraySchema = @Schema(description = "结算状态", implementation = PurchaseOrderPaymentStatus.class))
    private Set<PurchaseOrderPaymentStatus> paymentStatuses;

}
