package com.tyrael.kharazim.diagnosistreatment;

import com.tyrael.kharazim.diagnosistreatment.app.constant.DiagnosisTreatmentDictConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Tyrael Archangel
 * @since 2025/4/8
 */
@SpringBootTest
public class InitDictTest {

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

    @Test
    public void initDict() {
        dubboReferenceHolder.dictServiceApi.init(DiagnosisTreatmentDictConstants.PRESCRIPTION_CREATE_STATUS);
    }

}