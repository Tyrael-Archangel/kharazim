package com.tyrael.kharazim.application.customer.vo.customer;

import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/4/10
 */
@Data
public class PageCustomerRequest extends PageCommand {

    @Schema(description = "会员名")
    private String name;

}
