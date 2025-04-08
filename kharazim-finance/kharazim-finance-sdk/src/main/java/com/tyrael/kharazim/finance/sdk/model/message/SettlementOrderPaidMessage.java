package com.tyrael.kharazim.finance.sdk.model.message;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2025/4/8
 */
@Data
public class SettlementOrderPaidMessage {

    private String settlementOrderCode;
    private String sourcePrescriptionCode;
    private LocalDateTime paidTime;

}
