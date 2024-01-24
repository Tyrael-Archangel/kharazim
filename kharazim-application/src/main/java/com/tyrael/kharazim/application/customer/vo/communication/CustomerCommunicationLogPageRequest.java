package com.tyrael.kharazim.application.customer.vo.communication;

import com.tyrael.kharazim.common.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
@Data
public class CustomerCommunicationLogPageRequest extends PageCommand {

    @Schema(description = "会员编码")
    private String customerCode;

    @Schema(description = "客服人员编码")
    private String serviceUserCode;

}
