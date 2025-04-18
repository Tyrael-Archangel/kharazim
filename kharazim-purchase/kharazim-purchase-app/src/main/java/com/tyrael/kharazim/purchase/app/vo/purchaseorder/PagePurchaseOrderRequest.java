package com.tyrael.kharazim.purchase.app.vo.purchaseorder;

import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.purchase.app.constant.PurchaseDictConstants;
import com.tyrael.kharazim.purchase.app.enums.PurchasePaymentStatus;
import com.tyrael.kharazim.purchase.app.enums.PurchaseReceiveStatus;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
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

    /**
     * {@link PurchaseDictConstants#PURCHASE_RECEIVE_STATUS}
     */
    @ArraySchema(arraySchema = @Schema(
            description = "收货状态，字典编码: purchase_receive_status",
            implementation = PurchaseReceiveStatus.class))
    private Set<PurchaseReceiveStatus> receiveStatuses;

    /**
     * {@link PurchaseDictConstants#PURCHASE_PAYMENT_STATUS}
     */
    @ArraySchema(arraySchema = @Schema(
            description = "结算状态，字典编码: purchase_payment_status",
            implementation = PurchasePaymentStatus.class))
    private Set<PurchasePaymentStatus> paymentStatuses;

    @Schema(description = "创建开始日期", format = "yyyy-MM-dd", example = "2024-04-01")
    private LocalDate createDateMin;

    @Schema(description = "创建截止日期", format = "yyyy-MM-dd", example = "2024-04-30")
    private LocalDate createDateMax;
}
