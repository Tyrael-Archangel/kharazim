package com.tyrael.kharazim.application.system.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
public class DictItemDTO {

    @Schema(description = "字典项ID")
    private Long id;

    @Schema(description = "字典编码")
    private String typeCode;

    @Schema(description = "字典项名")
    private String name;

    @Schema(description = "字典项值")
    private String value;

    @Schema(description = "类型状态：1->启用;0->禁用")
    private Integer status;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(hidden = true)
    public void setStatusByEnable(Boolean enable) {
        this.status = Boolean.TRUE.equals(enable) ? 1 : 0;
    }

}
