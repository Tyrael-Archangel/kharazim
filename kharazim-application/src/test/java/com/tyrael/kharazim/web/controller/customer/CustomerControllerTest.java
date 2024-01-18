package com.tyrael.kharazim.web.controller.customer;

import com.google.common.collect.Sets;
import com.tyrael.kharazim.application.config.DictCodeConstants;
import com.tyrael.kharazim.application.customer.vo.*;
import com.tyrael.kharazim.application.system.dto.address.AddressTreeNodeDTO;
import com.tyrael.kharazim.application.system.dto.dict.SaveDictItemRequest;
import com.tyrael.kharazim.application.system.service.AddressQueryService;
import com.tyrael.kharazim.application.system.service.DictService;
import com.tyrael.kharazim.application.user.enums.UserCertificateTypeEnum;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
import com.tyrael.kharazim.common.dto.Pair;
import com.tyrael.kharazim.common.dto.Pairs;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
class CustomerControllerTest extends BaseControllerTest<CustomerController> {

    @Autowired
    private AddressQueryService addressQueryService;

    @Autowired
    private DictService dictService;

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
    void list() {
        ListCustomerRequest request = new ListCustomerRequest();
        request.setConditionType(ListCustomerRequest.QueryConditionType.NAME);
        request.setKeyword("迪亚波罗");
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

        Set<String> companyItems = dictService.findEnabledItems(DictCodeConstants.INSURANCE_COMPANY);
        if (companyItems.isEmpty()) {
            companyItems = addInsuranceCompanyDict();
        }
        List<String> companyDictValues = new ArrayList<>(companyItems);

        String customerCode = "CU0000000001";
        for (int i = 0; i < 5; i++) {
            String companyDictValue = companyDictValues.get(random.nextInt(companyDictValues.size()));
            String policyNumber = companyDictValue.toLowerCase() + i + System.currentTimeMillis();
            LocalDate duration = LocalDate.now().plusDays(1000 + random.nextInt(5000));
            String benefits = "福利：" + UUID.randomUUID();
            AddCustomerInsuranceRequest addCustomerInsuranceRequest = new AddCustomerInsuranceRequest();
            addCustomerInsuranceRequest.setCustomerCode(customerCode);
            addCustomerInsuranceRequest.setCompanyDictValue(companyDictValue);
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

        return addDictItems(DictCodeConstants.INSURANCE_COMPANY.getDictCode(), insuranceCompanies);
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
        super.performWhenCall(mockController.assignCustomerServiceUser(customerCode, serviceUserCode, super.mockAdmin()));
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
        super.performWhenCall(mockController.assignCustomerSalesConsultant(customerCode, salesConsultantCode, super.mockAdmin()));
    }

    @Test
    void customerTags() {
        String customerCode = "CU0000000001";
        super.performWhenCall(mockController.customerTags(customerCode));
    }

    @Test
    void addCustomerTag() {
        Set<String> customerTagDictItems = dictService.findEnabledItems(DictCodeConstants.CUSTOMER_TAG);
        if (customerTagDictItems.isEmpty()) {
            customerTagDictItems = addCustomerTagDict();
        }

        List<String> customerTags = new ArrayList<>(customerTagDictItems);
        Set<String> tagDictValues = Sets.newHashSet();
        for (int i = 0; i < 8; i++) {
            String customerTag = customerTags.get(random.nextInt(customerTags.size()));
            tagDictValues.add(customerTag);
        }

        String customerCode = "CU0000000001";
        AddCustomerTagRequest addCustomerTagRequest = new AddCustomerTagRequest();
        addCustomerTagRequest.setCustomerCode(customerCode);
        addCustomerTagRequest.setTagDictValues(tagDictValues);
        super.performWhenCall(mockController.addCustomerTag(addCustomerTagRequest, super.mockAdmin()));
    }

    private Set<String> addCustomerTagDict() {
        Pairs<String, String> customerTags = new Pairs<String, String>()
                .append("青铜", "bronze")
                .append("白银", "silver")
                .append("黄金", "gold")
                .append("铂金", "platinum")
                .append("钻石", "diamond")
                .append("健身爱好者", "fitness_enthusiasts")
                .append("注重保养", "pay_attention_to_maintenance")
                .append("身材火辣", "hot_figure")
                .append("高价值敏感", "high_value_sensitive")
                .append("信用良好", "good_credit")
                .append("南方人", "southerner")
                .append("北方人", "northerner")
                .append("外国人", "foreigner")
                .append("企业高管", "enterprise_senior_manager")
                .append("普通工人", "common_laborer")
                .append("家庭妇女", "housewife")
                .append("退休人员", "retiree")
                .append("消费能力强", "strong_spending_power");
        return addDictItems(DictCodeConstants.CUSTOMER_TAG.getDictCode(), customerTags);
    }

    private Set<String> addDictItems(String dictCode, Pairs<String, String> dictItems) {
        Set<String> dictItemValues = new LinkedHashSet<>();
        for (int i = 0; i < dictItems.size(); i++) {
            Pair<String, String> dictItem = dictItems.get(i);
            SaveDictItemRequest addDictItemRequest = new SaveDictItemRequest();
            addDictItemRequest.setTypeCode(dictCode);
            addDictItemRequest.setValue(dictItem.right());
            addDictItemRequest.setName(dictItem.left());
            addDictItemRequest.setSort(i + 1);
            addDictItemRequest.setEnable(Boolean.TRUE);
            dictService.addDictItem(addDictItemRequest, super.mockAdmin());
            dictItemValues.add(dictItem.right());
        }
        return dictItemValues;
    }

}
