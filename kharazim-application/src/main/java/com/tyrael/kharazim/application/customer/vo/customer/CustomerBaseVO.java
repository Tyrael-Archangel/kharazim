package com.tyrael.kharazim.application.customer.vo.customer;

import com.tyrael.kharazim.application.user.enums.UserCertificateTypeEnum;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
@Data
@Builder
public class CustomerBaseVO {

    @Schema(description = "会员编号")
    private String code;

    @Schema(description = "会员姓名")
    private String name;

    @Schema(description = "会员性别")
    private UserGenderEnum gender;

    @Schema(description = "生日年")
    private Integer birthYear;
    @Schema(description = "生日月")
    private Integer birthMonth;
    @Schema(description = "生日日")
    private Integer birthDayOfMonth;

    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "手机号是否已验证")
    private Boolean phoneVerified;

    @Schema(description = "证件类型")
    private UserCertificateTypeEnum certificateType;
    @Schema(description = "证件号码")
    private String certificateCode;

    @Schema(description = "微信号")
    private String wechatCode;
    @Schema(description = "微信名")
    private String wechatName;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "会员来源渠道字典键")
    private String sourceChannelDictKey;

    @Schema(description = "会员来源渠道")
    private String sourceChannel;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
