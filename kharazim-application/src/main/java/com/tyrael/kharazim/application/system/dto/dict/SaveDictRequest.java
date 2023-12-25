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
public class SaveDictRequest {

    @Schema(description = "字典编码", maxLength = 64)
    @NotBlank(message = "字典编码不能为空")
    @Size(max = 64, message = "字典编码超长")
    private String code;

    @Schema(description = "字典名", maxLength = 64)
    @NotBlank(message = "字典名不能为空")
    @Size(max = 64, message = "字典名超长")
    private String name;

    @Schema(description = "类型状态：1->启用;0->禁用")
    private Integer status;

    @Schema(description = "字典描述信息", maxLength = 255)
    @Size(max = 255, message = "字典描述信息超长")
    private String remark;

    @Schema(hidden = true)
    public boolean getEnable() {
        return status == null || status == 1;
    }

}
