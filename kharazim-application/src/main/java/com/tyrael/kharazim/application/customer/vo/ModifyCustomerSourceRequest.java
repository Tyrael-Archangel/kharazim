package com.tyrael.kharazim.application.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2024/1/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyCustomerSourceRequest {

    @Schema(description = "会员编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "会员编码不能为空")
    private String customerCode;

    @Schema(description = "推荐（引导）来源会员编码")
    @NotEmpty(message = "来源会员编码不能为空")
    private String sourceCustomerCode;

}
