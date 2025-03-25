package com.tyrael.kharazim.purchase.sdk.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/2/22
 */
@Getter
@Builder
@ToString
public class SupplierVO implements Serializable {

    private String code;

    private String name;

    private String remark;

    private String creator;

    private String creatorCode;

    private LocalDateTime createTime;

}
