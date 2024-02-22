package com.tyrael.kharazim.application.supplier.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Tyrael Archangel
 * @since 2024/2/22
 */
@Getter
@Builder
@ToString
public class SupplierVO {

    @Schema(description = "供应商编码")
    private String code;

    @Schema(description = "供应商名称")
    private String name;

    @Schema(description = "备注信息")
    private String remark;

}
