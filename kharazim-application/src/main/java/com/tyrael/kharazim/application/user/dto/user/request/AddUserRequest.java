package com.tyrael.kharazim.application.user.dto.user.request;

import com.tyrael.kharazim.application.user.enums.UserCertificateTypeEnum;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2023/12/27
 */
@Data
public class AddUserRequest {

    @Schema(description = "用户名")
    @NotEmpty(message = "用户名不能为空")
    private String name;

    @Schema(description = "用户昵称")
    @NotEmpty(message = "用户昵称不能为空")
    private String nickName;

    @Schema(description = "用户英文名")
    private String englishName;

    @Schema(description = "用户头像地址")
    private String avatar;

    @Schema(description = "性别")
    @NotNull(message = "请选择用户性别")
    private UserGenderEnum gender;

    @Schema(description = "手机号码")
    @Pattern(regexp = "^(\\+86)?(1[3-9])\\d{9}$", message = "手机号码格式有误")
    @NotBlank(message = "请输入手机号")
    private String phone;

    @Schema(description = "证件类型")
    @NotNull(message = "请选择证件类型")
    private UserCertificateTypeEnum certificateType;

    @Schema(description = "证件号码")
    @NotBlank(message = "请输入证件号码")
    private String certificateCode;

    @Schema(description = "备注")
    private String remark;

    @Schema(format = "yyyy-MM-dd", pattern = "yyyy-MM-dd", description = "生日，格式yyyy-MM-dd")
    @Parameter(example = "2028-08-11")
    @NotNull(message = "请选择生日")
    private LocalDate birthday;

    @Schema(description = "用户角色（岗位）编码")
    @NotNull(message = "请选择用户角色")
    private Set<String> roleCodes;

}
