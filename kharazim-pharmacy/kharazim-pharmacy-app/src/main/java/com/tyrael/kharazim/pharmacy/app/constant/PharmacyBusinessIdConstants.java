package com.tyrael.kharazim.pharmacy.app.constant;

import com.tyrael.kharazim.lib.idgenerator.BusinessIdConstant;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2025/2/18
 */
@Getter
@SuppressWarnings("unused")
public enum PharmacyBusinessIdConstants implements BusinessIdConstant<PharmacyBusinessIdConstants> {

    INBOUND_ORDER("入库单", 5, "IO"),
    INVENTORY_INBOUND("库存入库流水", "IIN"),
    ;

    private final String prefix;
    private final String desc;
    private final int bit;

    PharmacyBusinessIdConstants(String desc) {
        this(desc, DEFAULT_BIT);
    }

    PharmacyBusinessIdConstants(String desc, int bit) {
        this(desc, bit, null);
    }

    PharmacyBusinessIdConstants(String desc, String prefix) {
        this(desc, DEFAULT_BIT, prefix);
    }

    PharmacyBusinessIdConstants(String desc, int bit, String prefix) {
        this.prefix = prefix;
        this.desc = desc;
        this.bit = bit;
    }

}
