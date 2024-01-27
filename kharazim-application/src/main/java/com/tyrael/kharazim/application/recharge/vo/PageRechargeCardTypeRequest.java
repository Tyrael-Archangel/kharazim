package com.tyrael.kharazim.application.recharge.vo;

import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/27
 */
@Data
public class PageRechargeCardTypeRequest extends PageCommand {

    @Schema(description = "储值卡项名称", example = "5000卡")
    private String name;

    @Schema(description = "是否可以创建新卡")
    private Boolean canCreateNewCard;

}
