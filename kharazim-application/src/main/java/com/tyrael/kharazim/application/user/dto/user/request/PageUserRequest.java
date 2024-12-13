package com.tyrael.kharazim.application.user.dto.user.request;

import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageUserRequest extends PageCommand {

    @Schema(description = "用户名/昵称/手机号")
    private String keywords;

    @Schema(description = "状态，字典编码: enable_status", implementation = EnableStatusEnum.class)
    private EnableStatusEnum status;

    @Schema(description = "性别，字典编码: user_gender", implementation = UserGenderEnum.class)
    private UserGenderEnum gender;

}
