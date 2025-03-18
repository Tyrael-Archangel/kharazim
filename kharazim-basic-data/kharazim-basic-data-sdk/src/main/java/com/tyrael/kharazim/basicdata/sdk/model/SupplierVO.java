package com.tyrael.kharazim.basicdata.sdk.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/2/22
 */
@Getter
@Builder
@ToString
public class SupplierVO {

    private String code;

    private String name;

    private String remark;

    private String creator;

    private String creatorCode;

    private LocalDateTime createTime;

}
