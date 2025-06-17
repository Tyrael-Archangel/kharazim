package com.tyrael.kharazim.diagnosistreatment.controller;

import com.tyrael.kharazim.diagnosistreatment.app.vo.prescription.DailySalesModels;
import com.tyrael.kharazim.diagnosistreatment.app.vo.prescription.PagePrescriptionRequest;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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

    @Test
    void dailySales() {
        DailySalesModels.FilterCommand filter = new DailySalesModels.FilterCommand();
        filter.setStart(LocalDate.now());
        filter.setEnd(LocalDate.now().plusDays(30));
        super.performWhenCall(mockController.dailySales(filter));
    }

}