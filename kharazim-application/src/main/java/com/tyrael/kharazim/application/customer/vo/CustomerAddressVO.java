package com.tyrael.kharazim.application.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/15
 */
@Data
@Builder
public class CustomerAddressVO {

    @Schema(description = "会员地址ID")
    private Long customerAddressId;

    @Schema(description = "联系人")
    private String contact;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "省编码")
    private String provinceCode;
    @Schema(description = "省名称")
    private String provinceName;

    @Schema(description = "市编码")
    private String cityCode;
    @Schema(description = "市名称")
    private String cityName;

    @Schema(description = "县（区）编号")
    private String countyCode;
    @Schema(description = "县（区）名称")
    private String countyName;

    @Schema(description = "详细地址")
    private String detailAddress;

    @Schema(description = "是否为会员的默认地址")
    private Boolean defaultAddress;

}
