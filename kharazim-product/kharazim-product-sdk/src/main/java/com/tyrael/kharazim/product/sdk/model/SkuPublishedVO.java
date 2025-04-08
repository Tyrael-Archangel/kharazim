package com.tyrael.kharazim.product.sdk.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Tyrael Archangel
 * @since 2025/4/7
 */
@Data
public class SkuPublishedVO implements Serializable {

    private String clinicCode;
    private String skuCode;
    private BigDecimal price;

}
