package com.tyrael.kharazim.web.controller.customer;

import com.tyrael.kharazim.application.customer.vo.AddCustomerRequest;
import com.tyrael.kharazim.application.user.enums.UserCertificateTypeEnum;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
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

    @Test
    void add() {
        AddCustomerRequest addCustomerRequest = new AddCustomerRequest();
        addCustomerRequest.setName("迪亚波罗");
        addCustomerRequest.setGender(UserGenderEnum.MALE);
        addCustomerRequest.setBirthYear(1994);
        addCustomerRequest.setBirthMonth(1);
        addCustomerRequest.setBirthDayOfMonth(1);
        addCustomerRequest.setPhone("13511112222");
        addCustomerRequest.setCertificateType(UserCertificateTypeEnum.ID_CARD);
        addCustomerRequest.setCertificateCode("510123111122334444");
        addCustomerRequest.setSourceChannelDictValue("OFFLINE");
        addCustomerRequest.setRemark("恐惧魔王");
        super.performWhenCall(mockController.add(addCustomerRequest, super.mockAdmin()));
    }

    @Test
    void assignCustomerServiceUser() {
        String customerCode = "CU0000000001";
        String serviceUserCode = "U000002";
        super.performWhenCall(mockController.assignCustomerServiceUser(customerCode, serviceUserCode, super.mockAdmin()));
    }

    @Test
    void assignCustomerSalesConsultant() {
        String customerCode = "CU0000000001";
        String salesConsultantCode = "U000002";
        super.performWhenCall(mockController.assignCustomerSalesConsultant(customerCode, salesConsultantCode, super.mockAdmin()));
    }

}
