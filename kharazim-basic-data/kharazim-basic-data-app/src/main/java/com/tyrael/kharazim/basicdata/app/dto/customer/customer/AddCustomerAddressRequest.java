package com.tyrael.kharazim.basicdata.app.dto.customer.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCustomerAddressRequest {

    @Schema(description = "会员编码")
    private String customerCode;

    @Schema(description = "联系人", maxLength = 64)
    @Size(max = 64, message = "联系人超长")
    private String contact;

    @Schema(description = "联系人电话", maxLength = 32)
    @Size(max = 32, message = "联系人电话超长")
    private String contactPhone;

    @Schema(description = "省份编码")
    private String provinceCode;

    @Schema(description = "省份名称")
    private String provinceName;

    @Schema(description = "城市编码")
    private String cityCode;

    @Schema(description = "城市名称")
    private String cityName;

    @Schema(description = "县（区）编码")
    private String countyCode;

    @Schema(description = "县（区）名称")
    private String countyName;

    @Schema(description = "详细地址", maxLength = 255)
    @Size(max = 255, message = "详细地址超长")
    private String detailAddress;

    @Schema(description = "是否为会员的默认地址")
    private Boolean defaultAddress;

    @Schema(hidden = true)
    public boolean isCustomerDefaultAddress() {
        return Boolean.TRUE.equals(defaultAddress);
    }

}
