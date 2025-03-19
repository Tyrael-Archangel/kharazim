package com.tyrael.kharazim.product.app.vo.unit;

import com.tyrael.kharazim.base.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/2/20
 */
@Data
public class PageProductUnitRequest extends PageCommand {

    @Schema(description = "单位名称")
    private String name;

}