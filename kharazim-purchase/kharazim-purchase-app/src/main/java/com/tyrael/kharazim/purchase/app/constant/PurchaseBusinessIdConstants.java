package com.tyrael.kharazim.purchase.app.constant;

import com.tyrael.kharazim.lib.idgenerator.BusinessIdConstant;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2025/2/18
 */
@Getter
@SuppressWarnings("unused")
public enum PurchaseBusinessIdConstants implements BusinessIdConstant<PurchaseBusinessIdConstants> {

    SUPPLIER("供应商", "SU"),

    ;

    private final String prefix;
    private final String desc;
    private final int bit;

    PurchaseBusinessIdConstants(String desc) {
        this(desc, DEFAULT_BIT);
    }

    PurchaseBusinessIdConstants(String desc, int bit) {
        this(desc, bit, null);
    }

    PurchaseBusinessIdConstants(String desc, String prefix) {
        this(desc, DEFAULT_BIT, prefix);
    }

    PurchaseBusinessIdConstants(String desc, int bit, String prefix) {
        this.prefix = prefix;
        this.desc = desc;
        this.bit = bit;
    }

}
