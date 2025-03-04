package com.tyrael.kharazim.basicdata.app.dto.customer.customer;

import com.tyrael.kharazim.basicdata.app.enums.CustomerCertificateTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/15
 */
@Data
@Builder
public class CustomerSimpleVO {

    @Schema(description = "会员编码")
    private String code;

    @Schema(description = "会员名称")
    private String name;

    @Schema(description = "会员电话号码")
    private String phone;

    @Schema(description = "证件类型")
    private CustomerCertificateTypeEnum certificateType;

    @Schema(description = "证件号码")
    private String certificateCode;

}
