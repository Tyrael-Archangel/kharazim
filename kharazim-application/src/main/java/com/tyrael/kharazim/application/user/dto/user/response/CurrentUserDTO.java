package com.tyrael.kharazim.application.user.dto.user.response;

import com.tyrael.kharazim.application.user.dto.role.response.RoleDTO;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserDTO {

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

    @Schema(description = "用户头像地址URL")
    private String avatarUrl;

    @Schema(description = "性别")
    private UserGenderEnum gender;

    @Schema(description = "生日")
    private LocalDate birthday;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "角色（岗位）")
    private List<RoleDTO> roles;

    @Schema(description = "是否建议当前用户修改密码")
    private Boolean needChangePassword;

    @Schema(description = "上一次登录时间")
    private LocalDateTime lastLogin;

}
