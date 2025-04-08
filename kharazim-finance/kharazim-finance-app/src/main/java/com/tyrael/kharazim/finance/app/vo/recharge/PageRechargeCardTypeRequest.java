package com.tyrael.kharazim.finance.app.vo.recharge;

import com.tyrael.kharazim.base.dto.PageCommand;
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
