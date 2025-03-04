package com.tyrael.kharazim.basicdata.app.dto.customer.family;

import com.tyrael.kharazim.base.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/19
 */
@Data
public class PageFamilyRequest extends PageCommand {

    @Schema(description = "家庭名称")
    private String familyName;

    @Schema(description = "家庭编码")
    private String familyCode;

}
