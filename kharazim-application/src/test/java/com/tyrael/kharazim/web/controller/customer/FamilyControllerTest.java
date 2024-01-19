package com.tyrael.kharazim.web.controller.customer;

import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/1/19
 */
class FamilyControllerTest extends BaseControllerTest<FamilyController> {

    FamilyControllerTest() {
        super(FamilyController.class);
    }

    @Test
    void family() {
        String familyCode = "CF000001";
        super.performWhenCall(mockController.family(familyCode));
    }

}
