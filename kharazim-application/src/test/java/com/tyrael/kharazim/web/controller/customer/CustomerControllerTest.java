package com.tyrael.kharazim.web.controller.customer;

import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
class CustomerControllerTest extends BaseControllerTest<CustomerController> {

    CustomerControllerTest() {
        super(CustomerController.class);
    }

    @Test
    void findByCode() {
        String customerCode = "00000001";
        super.performWhenCall(mockController.findByCode(customerCode));
    }



}
