package com.tyrael.kharazim.application.customer.vo.communication;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
@Data
public class AddCustomerCommunicationLogRequest {

    @Schema(description = "会员编码")
    @NotBlank(message = "请指定会员")
    private String customerCode;

    @Schema(description = "沟通类型字典key")
    private String typeDictKey;

    @Schema(description = "沟通内容", maxLength = 1024)
    @Size(max = 1024, message = "沟通内容超长")
    private String content;

    @Schema(description = "沟通评价字典key")
    private String evaluateDictKey;

}
