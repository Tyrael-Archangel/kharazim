package com.tyrael.kharazim.application.system.dto.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/4
 */
@Data
public class QueryMenuRequest {

    @Schema(description = "关键字(菜单名称)")
    private String keywords;

}