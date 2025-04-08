package com.tyrael.kharazim.finance.app.vo.recharge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/2/1
 */
@Data
public class ListRechargeCardTypeRequest {

    @Schema(description = "储值卡项名称", example = "5000卡")
    private String name;

    @Schema(description = "是否可以创建新卡")
    private Boolean canCreateNewCard;

}
