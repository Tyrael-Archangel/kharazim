package com.tyrael.kharazim.application.user.dto.user.response;

import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import com.tyrael.kharazim.application.user.enums.UserCertificateTypeEnum;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户编码")
    private String code;

    @Schema(description = "用户名")
    private String name;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户英文名")
    private String englishName;

    @Schema(description = "用户头像地址")
    private String avatar;

    @Schema(description = "性别")
    private UserGenderEnum gender;

    @Schema(description = "生日")
    private LocalDate birthday;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "用户状态")
    private EnableStatusEnum status;

    @Schema(description = "微信号")
    private String wechatCode;

    @Schema(description = "微信名")
    private String wechatName;

    @Schema(description = "证件类型，" + UserCertificateTypeEnum.DESC)
    private UserCertificateTypeEnum certificateType;

    @Schema(description = "证件号码")
    private String certificateCode;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "角色（岗位）ID")
    private Long roleId;
    @Schema(description = "角色（岗位）编码")
    private String roleCode;
    @Schema(description = "角色（岗位）名")
    private String roleName;

    @Schema(description = "创建人")
    private String creator;
    @Schema(description = "创建人编码")
    private String creatorCode;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新人")
    private String updater;
    @Schema(description = "更新人编码")
    private String updaterCode;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
