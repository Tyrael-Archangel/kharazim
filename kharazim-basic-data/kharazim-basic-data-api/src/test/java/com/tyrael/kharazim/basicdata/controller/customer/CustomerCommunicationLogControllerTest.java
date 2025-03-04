package com.tyrael.kharazim.basicdata.controller.customer;

import com.tyrael.kharazim.basicdata.app.dto.customer.communication.CustomerCommunicationLogPageRequest;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
class CustomerCommunicationLogControllerTest extends BaseControllerTest<CustomerCommunicationLogController> {

    CustomerCommunicationLogControllerTest() {
        super(CustomerCommunicationLogController.class);
    }

    @Test
    void page() {
        CustomerCommunicationLogPageRequest pageRequest = new CustomerCommunicationLogPageRequest();
        pageRequest.setCustomerCode("CU0000000001");
        super.performWhenCall(mockController.page(pageRequest));
    }

}