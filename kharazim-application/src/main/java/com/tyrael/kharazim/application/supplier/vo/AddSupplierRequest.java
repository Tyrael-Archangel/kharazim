package com.tyrael.kharazim.application.supplier.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/2/22
 */
@Data
public class AddSupplierRequest {

    @Schema(description = "供应商名称")
    @NotBlank(message = "请输入供应商名称")
    private String name;

    @Schema(description = "备注信息")
    private String remark;

}
