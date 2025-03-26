package com.tyrael.kharazim.basicdata.app.dto.customer.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/1/17
 */
@Data
@Builder
public class CustomerServiceUserVO {

    @Schema(description = "会员编码")
    private String customerCode;

    @Schema(description = "专属客服编码")
    private String serviceUserCode;

    @Schema(description = "专属客服姓名")
    private String serviceUserName;

    @Schema(description = "专属客服头像")
    private String serviceUserAvatar;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
