package com.tyrael.kharazim.application.system.dto.dict;

import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
public class PageDictItemRequest extends PageCommand {

    @Schema(description = "字典项或字典值名称关键字")
    private String keywords;

    @Schema(description = "字典编码")
    private String typeCode;

}
