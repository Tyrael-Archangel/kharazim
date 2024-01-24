package com.tyrael.kharazim.application.customer.vo.communication;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCommunicationLogVO {

    @Schema(description = "沟通类型")
    private String type;

    @Schema(description = "沟通类型字典值")
    private String typeDictValue;

    @Schema(description = "会员编码")
    private String customerCode;

    @Schema(description = "会员名称")
    private String customerName;

    @Schema(description = "客服人员编码")
    private String serviceUserCode;

    @Schema(description = "客服人员名称")
    private String serviceUserName;

    @Schema(description = "沟通内容")
    private String content;

    @Schema(description = "沟通评价")
    private String evaluate;

    @Schema(description = "沟通评价字典值")
    private String evaluateDictValue;

    @Schema(description = "沟通时间")
    private LocalDateTime communicationTime;

    @Schema(description = "创建人")
    private String creator;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
