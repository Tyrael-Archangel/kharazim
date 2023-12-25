package com.tyrael.kharazim.application.system.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
public class SaveDictItemRequest {

    @Schema(description = "字典编码")
    @NotBlank(message = "字典编码不能为空")
    private String typeCode;

    @Schema(description = "字典项名", maxLength = 64)
    @NotBlank(message = "字典项名不能为空")
    @Size(max = 64, message = "字典项名超长")
    private String name;

    @Schema(description = "字典项值", maxLength = 64)
    @NotBlank(message = "字典项值不能为空")
    @Size(max = 64, message = "字典项值超长")
    private String value;

    @Schema(description = "类型状态：1->启用;0->禁用")
    private Integer status;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(hidden = true)
    public boolean getEnable() {
        return status == null || status == 1;
    }
}
