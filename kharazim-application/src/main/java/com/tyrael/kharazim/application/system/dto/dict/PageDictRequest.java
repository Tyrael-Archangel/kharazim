package com.tyrael.kharazim.application.system.dto.dict;

import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
public class PageDictRequest extends PageCommand {

    @Schema(description = "字典名或字典编码关键字，模糊搜索")
    private String keywords;

}
