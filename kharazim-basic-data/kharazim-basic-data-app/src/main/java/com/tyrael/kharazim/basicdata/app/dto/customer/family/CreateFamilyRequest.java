package com.tyrael.kharazim.basicdata.app.dto.customer.family;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/19
 */
@Data
public class CreateFamilyRequest {

    @Schema(description = "家庭名称", maxLength = 64)
    @Size(max = 64, message = "家庭名称超长")
    private String familyName;

    @Schema(description = "家庭户主编码")
    @NotBlank(message = "请为家庭指定户主")
    private String leaderCustomerCode;

    @Schema(description = "备注信息", maxLength = 1024)
    @Size(max = 1024, message = "备注信息超长")
    private String remark;

}
