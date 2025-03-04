package com.tyrael.kharazim.basicdata.app.dto.customer.communication;

import com.tyrael.kharazim.base.dto.PageCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

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

    @Schema(description = "沟通类型字典键，字典编码: communication_type")
    private String typeDictKey;

    @Schema(description = "沟通评价字典键，字典编码: communication_evaluate")
    private String evaluateDictKey;

    @Schema(description = "创建开始时间")
    private LocalDateTime createdBegin;

    @Schema(description = "创建结束时间")
    private LocalDateTime createdEnd;

    @Schema(description = "沟通开始时间")
    private LocalDateTime communicationBegin;

    @Schema(description = "沟通结束时间")
    private LocalDateTime communicationEnd;
}
