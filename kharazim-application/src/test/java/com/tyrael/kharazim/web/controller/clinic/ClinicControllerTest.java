package com.tyrael.kharazim.web.controller.clinic;

import com.tyrael.kharazim.application.clinic.vo.PageClinicRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2023/12/31
 */
class ClinicControllerTest extends BaseControllerTest<ClinicController> {

    public ClinicControllerTest() {
        super(ClinicController.class);
    }

    @Test
    void page() {
        PageClinicRequest pageRequest = new PageClinicRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

}