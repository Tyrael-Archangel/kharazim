package com.tyrael.kharazim.diagnosistreatment.app.constant;

import com.tyrael.kharazim.basicdata.sdk.model.DictConstant;
import com.tyrael.kharazim.diagnosistreatment.app.enums.PrescriptionCreateStatus;

import static com.tyrael.kharazim.basicdata.sdk.model.DictConstant.dict;

/**
 * @author Tyrael Archangel
 * @since 2025/2/21
 */
public interface DiagnosisTreatmentDictConstants {

    // @formatter:off

    DictConstant PRESCRIPTION_CREATE_STATUS = dict("prescription_create_status", PrescriptionCreateStatus.class, "处方创建状态");

    // @formatter:on

}
