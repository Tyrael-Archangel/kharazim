package com.tyrael.kharazim.diagnosistreatment.app.constant;

import com.tyrael.kharazim.lib.idgenerator.BusinessIdConstant;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2025/2/18
 */
@Getter
@SuppressWarnings("unused")
public enum DiagnosisTreatmentBusinessIdConstants implements BusinessIdConstant<DiagnosisTreatmentBusinessIdConstants> {

    PRESCRIPTION("处方", 4, "RX");

    private final String prefix;
    private final String desc;
    private final int bit;

    DiagnosisTreatmentBusinessIdConstants(String desc) {
        this(desc, DEFAULT_BIT);
    }

    DiagnosisTreatmentBusinessIdConstants(String desc, int bit) {
        this(desc, bit, null);
    }

    DiagnosisTreatmentBusinessIdConstants(String desc, String prefix) {
        this(desc, DEFAULT_BIT, prefix);
    }

    DiagnosisTreatmentBusinessIdConstants(String desc, int bit, String prefix) {
        this.prefix = prefix;
        this.desc = desc;
        this.bit = bit;
    }

}
