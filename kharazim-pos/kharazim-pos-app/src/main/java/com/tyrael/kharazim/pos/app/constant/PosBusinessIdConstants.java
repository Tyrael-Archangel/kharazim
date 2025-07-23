package com.tyrael.kharazim.pos.app.constant;

import com.tyrael.kharazim.lib.idgenerator.BusinessIdConstant;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2025/7/23
 */
@Getter
@SuppressWarnings("unused")
public enum PosBusinessIdConstants implements BusinessIdConstant<PosBusinessIdConstants> {

    CLINIC_ORDER("诊所订单", 5, "CO"),
    ;

    private final String prefix;
    private final String desc;
    private final int bit;

    PosBusinessIdConstants(String desc) {
        this(desc, DEFAULT_BIT);
    }

    PosBusinessIdConstants(String desc, int bit) {
        this(desc, bit, null);
    }

    PosBusinessIdConstants(String desc, String prefix) {
        this(desc, DEFAULT_BIT, prefix);
    }

    PosBusinessIdConstants(String desc, int bit, String prefix) {
        this.prefix = prefix;
        this.desc = desc;
        this.bit = bit;
    }

}
