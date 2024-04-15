package com.tyrael.kharazim.application.customer.vo.customer;

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
public class CustomerSalesConsultantVO {

    @Schema(description = "会员编码")
    private String customerCode;

    @Schema(description = "专属销售顾问编码")
    private String salesConsultantCode;

    @Schema(description = "专属销售顾问姓名")
    private String salesConsultantName;

    @Schema(description = "专属销售顾问头像")
    private String salesConsultantAvatar;

    @Schema(description = "专属销售顾问头像链接")
    private String salesConsultantAvatarUrl;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
