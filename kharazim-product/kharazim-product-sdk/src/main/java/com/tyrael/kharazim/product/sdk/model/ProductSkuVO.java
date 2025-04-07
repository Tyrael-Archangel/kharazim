package com.tyrael.kharazim.product.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
@Data
public class ProductSkuVO implements Serializable {

    private String code;

    private String name;

    private String categoryCode;

    private String categoryName;

    private String categoryFullName;

    private String supplierCode;

    private String supplierName;

    private String unitCode;

    private String unitName;

    private String defaultImage;

    private List<String> images;

    private String description;

    private List<AttributeVO> attributes;

    private String attributesDesc;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttributeVO implements Serializable {

        private String name;
        private String value;

    }
}
