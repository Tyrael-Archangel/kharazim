package com.tyrael.kharazim.web.controller.prescription;

import com.google.common.collect.Lists;
import com.tyrael.kharazim.application.prescription.vo.CreatePrescriptionRequest;
import com.tyrael.kharazim.application.prescription.vo.PagePrescriptionRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    void create() {
        CreatePrescriptionRequest request = new CreatePrescriptionRequest();
        request.setCustomerCode("CU0000000002");
        request.setClinicCode("CL000001");
        request.setRemark("处方备注，先天下之忧而忧");

        List<CreatePrescriptionRequest.Product> products = Lists.newArrayList(
                new CreatePrescriptionRequest.Product("P240311000001", 3));
        request.setProducts(products);

        super.performWhenCall(mockController.create(request));
    }

}