package com.tyrael.kharazim.basicdata.app.dto.customer.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2024/1/18
 */
@Data
public class AddCustomerTagRequest {

    @Schema(description = "会员编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "会员编码不能为空")
    private String customerCode;

    @Schema(description = "会员标签字典键，字典编码：customer_tag", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "请选择会员标签")
    private Set<String> tagDictKeys;

}
