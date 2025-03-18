package com.tyrael.kharazim.basicdata.app.dto.supplier;

import com.tyrael.kharazim.base.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/2/22
 */
@Data
public class PageSupplierRequest extends PageCommand {

    @Schema(description = "供应商名称")
    private String name;

}
