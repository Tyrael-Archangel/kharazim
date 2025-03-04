package com.tyrael.kharazim.basicdata.app.dto.customer.customer;

import com.tyrael.kharazim.base.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/4/10
 */
@Data
public class PageCustomerRequest extends PageCommand {

    @Schema(description = "会员编码")
    private String code;

    @Schema(description = "会员名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

}
