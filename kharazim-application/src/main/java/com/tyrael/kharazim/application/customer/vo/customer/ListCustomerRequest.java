package com.tyrael.kharazim.application.customer.vo.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/15
 */
@Data
public class ListCustomerRequest {

    @Schema(description = "查询条件类型，NAME-姓名，PHONE-电话，CERTIFICATE-证件号码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "请指定查询条件类型")
    private QueryConditionType conditionType;

    @Schema(description = "查询条件，姓名、电话、证件号码")
    private String keyword;

    public enum QueryConditionType {
        /**
         * 姓名
         */
        NAME,
        /**
         * 电话
         */
        PHONE,
        /**
         * 证件号码
         */
        CERTIFICATE
    }

}
