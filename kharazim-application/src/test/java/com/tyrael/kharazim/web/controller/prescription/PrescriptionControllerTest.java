package com.tyrael.kharazim.web.controller.prescription;

import com.tyrael.kharazim.application.prescription.vo.PagePrescriptionRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/3/28
 */
class PrescriptionControllerTest extends BaseControllerTest<PrescriptionController> {

    PrescriptionControllerTest() {
        super(PrescriptionController.class);
    }

    @Test
    void page() {
        PagePrescriptionRequest pageRequest = new PagePrescriptionRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

    @Test
    void detail() {
        String code = "RX202403280001";
        super.performWhenCall(mockController.detail(code));
    }

}