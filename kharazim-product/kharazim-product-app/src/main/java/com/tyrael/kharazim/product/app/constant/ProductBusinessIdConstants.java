package com.tyrael.kharazim.product.app.constant;

import com.tyrael.kharazim.lib.idgenerator.BusinessIdConstant;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2025/2/18
 */
@Getter
@SuppressWarnings("unused")
public enum ProductBusinessIdConstants implements BusinessIdConstant<ProductBusinessIdConstants> {

    PRODUCT_CATEGORY("商品分类", 3, "PC"),
    PRODUCT_UNIT("商品单位编码", 4, "UT"),
    SKU("SKU", 6),
    SKU_PUBLISH("商品发布", 5, "SPB");

    private final String prefix;
    private final String desc;
    private final int bit;

    ProductBusinessIdConstants(String desc) {
        this(desc, DEFAULT_BIT);
    }

    ProductBusinessIdConstants(String desc, int bit) {
        this(desc, bit, null);
    }

    ProductBusinessIdConstants(String desc, String prefix) {
        this(desc, DEFAULT_BIT, prefix);
    }

    ProductBusinessIdConstants(String desc, int bit, String prefix) {
        this.prefix = prefix;
        this.desc = desc;
        this.bit = bit;
    }

}
