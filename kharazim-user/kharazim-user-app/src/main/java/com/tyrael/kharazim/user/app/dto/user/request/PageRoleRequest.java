package com.tyrael.kharazim.user.app.dto.user.request;

import com.tyrael.kharazim.lib.base.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageRoleRequest extends PageCommand {

    @Schema(description = "角色（岗位）名关键字（模糊搜索）")
    private String keywords;

}
