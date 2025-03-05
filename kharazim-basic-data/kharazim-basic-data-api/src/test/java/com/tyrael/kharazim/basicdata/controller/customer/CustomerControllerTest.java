package com.tyrael.kharazim.basicdata.controller.customer;

import com.tyrael.kharazim.base.dto.Pair;
import com.tyrael.kharazim.base.dto.Pairs;
import com.tyrael.kharazim.basicdata.BasicDataApiApplication;
import com.tyrael.kharazim.basicdata.app.constant.BasicDataDictConstants;
import com.tyrael.kharazim.basicdata.app.dto.address.AddressTreeNodeDTO;
import com.tyrael.kharazim.basicdata.app.dto.customer.customer.AddCustomerAddressRequest;
import com.tyrael.kharazim.basicdata.app.dto.customer.customer.AddCustomerInsuranceRequest;
import com.tyrael.kharazim.basicdata.app.dto.customer.customer.ListCustomerRequest;
import com.tyrael.kharazim.basicdata.app.dto.customer.customer.PageCustomerRequest;
import com.tyrael.kharazim.basicdata.app.service.address.AddressQueryService;
import com.tyrael.kharazim.basicdata.sdk.model.DictItemVO;
import com.tyrael.kharazim.basicdata.sdk.service.DictServiceApi;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import com.tyrael.kharazim.user.sdk.model.MockAuthUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
@SpringBootTest(classes = BasicDataApiApplication.class)
class CustomerControllerTest extends BaseControllerTest<CustomerController> {

    @Autowired
    private AddressQueryService addressQueryService;

    @Autowired
    private DictServiceApi dictServiceApi;

    public CustomerControllerTest() {
        super(CustomerController.class);
    }

    @Test
    void findByCode() {
        String customerCode = "CU0000000001";
        super.performWhenCall(mockController.findByCode(customerCode));
    }

    @Test
    void page() {
        PageCustomerRequest pageRequest = new PageCustomerRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

    @Test
    void list() {
        ListCustomerRequest request = new ListCustomerRequest();
        request.setConditionType(ListCustomerRequest.QueryConditionType.NAME);
        super.performWhenCall(mockController.list(request));
    }

    @Test
    void addresses() {
        String customerCode = "CU0000000001";
        super.performWhenCall(mockController.addresses(customerCode));
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
    void deleteCustomerAddress() {
        String customerCode = "CU0000000001";
        long customerAddressId = 5L;
        super.performWhenCall(mockController.deleteCustomerAddress(customerCode, customerAddressId));
    }

    @Test
    void markAddressDefault() {
        String customerCode = "CU0000000001";
        long customerAddressId = 3L;
        super.performWhenCall(mockController.markAddressDefault(customerCode, customerAddressId));
    }

    @Test
    void insurances() {
        String customerCode = "CU0000000001";
        super.performWhenCall(mockController.insurances(customerCode));
    }

    @Test
    void addInsurance() {

        Set<String> companyItems = dictServiceApi.dictItemKeys(BasicDataDictConstants.INSURANCE_COMPANY);
        if (companyItems.isEmpty()) {
            companyItems = addInsuranceCompanyDict();
        }
        List<String> companyDictKeys = new ArrayList<>(companyItems);

        String customerCode = "CU0000000001";
        for (int i = 0; i < 5; i++) {
            String companyDictKey = companyDictKeys.get(random.nextInt(companyDictKeys.size()));
            String policyNumber = companyDictKey.toLowerCase() + i + System.currentTimeMillis();
            LocalDate duration = LocalDate.now().plusDays(1000 + random.nextInt(5000));
            String benefits = "福利：" + UUID.randomUUID();
            AddCustomerInsuranceRequest addCustomerInsuranceRequest = new AddCustomerInsuranceRequest();
            addCustomerInsuranceRequest.setCustomerCode(customerCode);
            addCustomerInsuranceRequest.setCompanyDictKey(companyDictKey);
            addCustomerInsuranceRequest.setPolicyNumber(policyNumber);
            addCustomerInsuranceRequest.setDuration(duration);
            addCustomerInsuranceRequest.setBenefits(benefits);
            super.performWhenCall(mockController.addInsurance(addCustomerInsuranceRequest));
        }
    }

    private Set<String> addInsuranceCompanyDict() {

        Pairs<String, String> insuranceCompanies = new Pairs<String, String>()
                .append("AIA | 友邦", "AIA")
                .append("Allianz | 安联", "ALLIANZ")
                .append("AXA | 安盛", "AXA")
                .append("Bupa | 保柏", "BUPA")
                .append("Cigna | 信诺", "CIGNA")
                .append("Cigna & CMB | 招商信诺", "CIGNACMB")
                .append("MSH | 万欣和", "MSH")
                .append("PINGAN | 中国平安", "PINGAN");

        return addDictItems(BasicDataDictConstants.INSURANCE_COMPANY.getCode(), insuranceCompanies);
    }

    @Test
    void deleteCustomerInsurance() {
        String customerCode = "CU0000000001";
        long customerInsuranceId = 1L;
        super.performWhenCall(mockController.deleteCustomerInsurance(customerCode, customerInsuranceId));
    }

    @Test
    void markInsuranceDefault() {
        String customerCode = "CU0000000001";
        long customerInsuranceId = 2L;
        super.performWhenCall(mockController.markInsuranceDefault(customerCode, customerInsuranceId));
    }

    @Test
    void customerService() {
        String customerCode = "CU0000000001";
        super.performWhenCall(mockController.customerService(customerCode));
    }

    @Test
    void assignCustomerServiceUser() {
        String customerCode = "CU0000000001";
        String serviceUserCode = "U000002";
        super.performWhenCall(mockController.assignCustomerServiceUser(customerCode, serviceUserCode, MockAuthUser.mock()));
    }

    @Test
    void customerSalesConsultant() {
        String customerCode = "CU0000000001";
        super.performWhenCall(mockController.customerSalesConsultant(customerCode));
    }

    @Test
    void assignCustomerSalesConsultant() {
        String customerCode = "CU0000000001";
        String salesConsultantCode = "U000002";
        super.performWhenCall(mockController.assignCustomerSalesConsultant(customerCode, salesConsultantCode, MockAuthUser.mock()));
    }

    @Test
    void customerTags() {
        String customerCode = "CU0000000001";
        super.performWhenCall(mockController.customerTags(customerCode));
    }

    @Test
    void removeCustomerTag() {
        String customerCode = "CU0000000001";
        String tagDictKey = "common_laborer";
        super.performWhenCall(mockController.removeCustomerTag(customerCode, tagDictKey));
    }

    private Set<String> addDictItems(String dictCode, Pairs<String, String> dictItems) {
        Set<String> dictItemKeys = new LinkedHashSet<>();
        for (int i = 0; i < dictItems.size(); i++) {
            Pair<String, String> dictItem = dictItems.get(i);
            DictItemVO addDictItemRequest = new DictItemVO();
            addDictItemRequest.setDictCode(dictCode);
            addDictItemRequest.setKey(dictItem.left());
            addDictItemRequest.setValue(dictItem.right());
            addDictItemRequest.setSort(i + 1);
            dictServiceApi.addDictItem(addDictItemRequest);
            dictItemKeys.add(dictItem.left());
        }
        return dictItemKeys;
    }

}
