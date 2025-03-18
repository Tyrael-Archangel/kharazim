package com.tyrael.kharazim.basicdata.app.dto.supplier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/2/26
 */
@Data
public class ListSupplierRequest {

    @Schema(description = "供应商名称")
    private String name;

}
