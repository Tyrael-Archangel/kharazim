package com.tyrael.kharazim.application.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/18
 */
@Data
public class CustomerTagVO {

    @Schema(description = "会员标签名")
    private String tagName;

    @Schema(description = "会员标签字典值")
    private String tagDictValue;

}
