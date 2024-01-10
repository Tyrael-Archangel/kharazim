package com.tyrael.kharazim.web.controller.customer;

import com.tyrael.kharazim.application.customer.vo.AddCustomerAddressRequest;
import com.tyrael.kharazim.application.customer.vo.AddCustomerRequest;
import com.tyrael.kharazim.application.system.dto.address.AddressTreeNodeDTO;
import com.tyrael.kharazim.application.system.service.AddressQueryService;
import com.tyrael.kharazim.application.user.enums.UserCertificateTypeEnum;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
class CustomerControllerTest extends BaseControllerTest<CustomerController> {

    @Autowired
    private AddressQueryService addressQueryService;

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
    void addAddress() {

        for (int i = 0; i < 8; i++) {
            String customerCode = "CU0000000001";
            AddCustomerAddressRequest addCustomerAddressRequest = new AddCustomerAddressRequest();
            addCustomerAddressRequest.setCustomerCode(customerCode);
            addCustomerAddressRequest.setContact("泰瑞尔·大天使");
            addCustomerAddressRequest.setContactPhone("13511112222");

            AddressTreeNodeDTO province = null;
            AddressTreeNodeDTO city = null;
            AddressTreeNodeDTO county = null;
            Random random = new Random();
            List<AddressTreeNodeDTO> provinces = addressQueryService.fullTree();
            if (provinces != null && !provinces.isEmpty()) {
                province = provinces.get(random.nextInt(provinces.size()));
            }
            if (province != null) {
                List<AddressTreeNodeDTO> cities = new ArrayList<>(province.getChildren());
                city = cities.get(random.nextInt(cities.size()));
            }
            if (city != null) {
                List<AddressTreeNodeDTO> counties = new ArrayList<>(city.getChildren());
                county = counties.get(random.nextInt(counties.size()));
            }

            addCustomerAddressRequest.setProvinceCode(province == null ? null : province.getCode());
            addCustomerAddressRequest.setProvinceName(province == null ? null : province.getName());
            addCustomerAddressRequest.setCityCode(city == null ? null : city.getCode());
            addCustomerAddressRequest.setCityName(city == null ? null : city.getName());
            addCustomerAddressRequest.setCountyCode(county == null ? null : county.getCode());
            addCustomerAddressRequest.setCountyName(county == null ? null : county.getName());
            addCustomerAddressRequest.setDetailAddress("xxx街道xxx小区x栋x单元");
            addCustomerAddressRequest.setDefaultAddress(true);
            super.performWhenCall(mockController.addAddress(addCustomerAddressRequest));
        }
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
