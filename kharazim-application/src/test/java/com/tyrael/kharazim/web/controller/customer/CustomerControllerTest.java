package com.tyrael.kharazim.web.controller.customer;

import com.google.common.collect.Sets;
import com.tyrael.kharazim.application.config.DictCodeConstants;
import com.tyrael.kharazim.application.customer.service.CustomerService;
import com.tyrael.kharazim.application.customer.vo.customer.*;
import com.tyrael.kharazim.application.system.dto.address.AddressTreeNodeDTO;
import com.tyrael.kharazim.application.system.dto.dict.SaveDictItemRequest;
import com.tyrael.kharazim.application.system.service.AddressQueryService;
import com.tyrael.kharazim.application.system.service.DictService;
import com.tyrael.kharazim.application.user.dto.user.request.ListUserRequest;
import com.tyrael.kharazim.application.user.dto.user.response.UserDTO;
import com.tyrael.kharazim.application.user.enums.UserCertificateTypeEnum;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
import com.tyrael.kharazim.application.user.service.UserService;
import com.tyrael.kharazim.common.dto.Pair;
import com.tyrael.kharazim.common.dto.Pairs;
import com.tyrael.kharazim.common.util.CollectionUtils;
import com.tyrael.kharazim.mock.MockRandomPoetry;
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

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    CustomerControllerTest() {
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
    void add() {

        List<AddressTreeNodeDTO> addressTree = addressQueryService.fullTree();
        Pairs<String, UserGenderEnum> mockUsers = this.mockUsers();
        Set<String> mockCommunityNames = this.mockCommunityNames();
        List<String> serviceUserCodes = userService.list(new ListUserRequest())
                .stream()
                .map(UserDTO::getCode)
                .toList();
        List<String> companyItems = new ArrayList<>(dictService.findEnabledItems(DictCodeConstants.INSURANCE_COMPANY));

        for (Pair<String, UserGenderEnum> userNameAndGender : mockUsers) {

            String name = userNameAndGender.left();
            UserGenderEnum gender = userNameAndGender.right();
            LocalDate birthday = LocalDate.parse("1980-01-01")
                    .plusDays(random.nextInt(40 * 365));
            String phone = "13511112222";

            AddCustomerRequest addCustomerRequest = new AddCustomerRequest();
            addCustomerRequest.setName(name);
            addCustomerRequest.setGender(gender);
            addCustomerRequest.setBirthYear(birthday.getYear());
            addCustomerRequest.setBirthMonth(birthday.getMonthValue());
            addCustomerRequest.setBirthDayOfMonth(birthday.getDayOfMonth());
            addCustomerRequest.setPhone(phone);
            addCustomerRequest.setCertificateType(UserCertificateTypeEnum.ID_CARD);
            addCustomerRequest.setCertificateCode("510123111122334444");
            addCustomerRequest.setSourceChannelDictValue("OFFLINE");
            addCustomerRequest.setRemark(MockRandomPoetry.random());
            addCustomerRequest.setServiceUserCode(CollectionUtils.random(serviceUserCodes));
            addCustomerRequest.setSalesConsultantCode(CollectionUtils.random(serviceUserCodes));
            addCustomerRequest.setCustomerAddresses(this.mockAddress(name, phone, addressTree, mockCommunityNames));
            addCustomerRequest.setCustomerInsurances(this.mockInsurances(companyItems));
            super.performWhenCall(mockController.add(addCustomerRequest, super.mockAdmin()));
        }

    }

    private Pairs<String, UserGenderEnum> mockUsers() {
        Set<String> familyNames = familyNames();
        List<String> boyNames = new ArrayList<>(popularBoyNames());
        List<String> girlNames = new ArrayList<>(popularGirlNames());
        int totalUserCount = random.nextInt(400) + 400;

        Pairs<String, UserGenderEnum> mockedUsers = new Pairs<>();
        Set<String> fullNames = new HashSet<>();
        for (int i = 0; i < totalUserCount; i++) {
            boolean boy = random.nextBoolean();
            List<String> nameWords = boy ? boyNames : girlNames;
            UserGenderEnum gender = boy ? UserGenderEnum.MALE : UserGenderEnum.FEMALE;

            String fullName = CollectionUtils.random(familyNames) + CollectionUtils.random(nameWords);
            if (random.nextInt(100) > 65) {
                fullName += CollectionUtils.random(nameWords);
            }

            if (fullNames.add(fullName)) {
                mockedUsers.append(fullName, gender);
            }
        }
        return mockedUsers;
    }

    private Set<String> familyNames() {
        return Set.of(
                "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "杨", "朱", "秦",
                "何", "吕", "张", "曹", "姜", "邹", "苏", "潘", "葛", "范", "彭", "鲁", "马",
                "袁", "唐", "薛", "罗", "梁", "江", "黄", "宋", "胡", "贾", "杜", "阮");
    }

    private Set<String> popularBoyNames() {
        return Set.of(
                "轩", "晟", "浩", "曦", "浚", "坚", "景", "源", "彦", "威", "明", "迅", "霖", "坤", "震", "承", "辉", "道",
                "佳", "黎", "毅", "恒", "瀚", "伟", "佑", "田", "枫", "诚", "信", "锋", "安", "郴", "德", "栋", "海", "灏",
                "凯", "宗", "启", "基", "同", "洋", "纶", "瑜", "榕", "翔", "咏", "智", "康", "桦", "珩", "麟", "旭", "朝",
                "勇", "谦", "然", "驰", "亦", "永", "衡", "卫", "绍", "晨", "祺", "金", "晏", "昌", "志", "平", "嘉", "哲",
                "鑫", "齐", "翰", "达", "卓", "学", "霆", "东", "磊", "彬", "顺", "森", "澄", "冠", "龙", "岳", "耀", "天",
                "西", "善", "廷", "沐", "建", "铭", "世", "林", "宏", "瑾", "宥", "鹏", "昊", "振", "博", "烽", "煊", "宁",
                "清", "维", "裕", "祥", "儒", "勤", "圣", "凡", "良", "迪", "璟", "锡", "元", "烨", "熙", "阳", "强", "茂",
                "航", "云", "兴", "梁", "雄", "友", "星", "润", "新", "希", "盛", "杰", "昱", "蓝", "鸣", "波", "钧", "骏",
                "庆", "辰", "思", "雨", "超", "昀", "亮", "聪", "渝", "奕", "炫", "松", "琛", "贤", "腾", "光", "京", "泰",
                "成", "玮", "杭", "杉", "瑄", "炎", "俊", "焜", "正", "利", "尊", "锦", "灿");
    }

    private Set<String> popularGirlNames() {
        return Set.of(
                "诗", "琳", "若", "婉", "勤", "凡", "瑾", "汐", "琬", "兮", "锦", "茜", "岚", "姝", "熙", "莺", "馨", "静",
                "婕", "蝶", "瑞", "新", "菲", "念", "媚", "忆", "姣", "怜", "渟", "薇", "疏", "映", "慧", "菁", "娇", "蓓",
                "雅", "含", "滢", "歆", "燕", "毓", "玉", "澜", "晓", "萱", "璐", "缨", "盼", "玲", "冬", "蕾", "慕", "笑",
                "怡", "缦", "馥", "思", "露", "芷", "瑶", "卿", "菡", "语", "婧", "玥", "芙", "蓝", "灵", "佩", "桃", "素",
                "可", "钰", "洋", "初", "茗", "丽", "华", "艺", "清", "梵", "娜", "美", "青", "梦", "咏", "夏", "雪", "欢",
                "筠", "宁", "莎", "贝", "兰", "逸", "涵", "如", "倩", "彤", "叶", "雯", "彩", "采", "瑗", "依", "睿", "佳",
                "影", "蕊", "枫", "秀", "琪", "悦", "淇", "欣", "元", "聪", "安", "婷", "亦", "凌", "嘉", "妍", "婵", "琦",
                "羽", "希", "琼", "惠", "彦", "姬", "卉", "颖", "舒", "曼", "荷", "文", "沛", "宛", "桦", "苑", "洁", "媛",
                "雨", "函", "珊", "旋", "晶", "檬", "迎", "冰", "蓉", "娅", "莉", "娴", "惜", "凝", "觅", "丹", "云", "南",
                "乐", "纤", "娣", "妙", "伊", "易", "黛", "双", "芃", "碧");
    }

    private List<AddCustomerAddressRequest> mockAddress(String userName,
                                                        String phone,
                                                        List<AddressTreeNodeDTO> addressTree,
                                                        Set<String> mockCommunityNames) {

        List<AddCustomerAddressRequest> mockAddresses = new ArrayList<>();

        int count = random.nextInt(6) + 1;
        for (int i = 0; i < count; i++) {
            AddCustomerAddressRequest request = new AddCustomerAddressRequest();
            request.setContact(userName);
            request.setContactPhone(phone);

            AddressTreeNodeDTO province = null;
            AddressTreeNodeDTO city = null;
            AddressTreeNodeDTO county = null;
            if (addressTree != null && !addressTree.isEmpty()) {
                province = addressTree.get(random.nextInt(addressTree.size()));
            }
            if (province != null) {
                List<AddressTreeNodeDTO> cities = new ArrayList<>(province.getChildren());
                city = cities.get(random.nextInt(cities.size()));
            }
            if (city != null) {
                List<AddressTreeNodeDTO> counties = new ArrayList<>(city.getChildren());
                county = counties.get(random.nextInt(counties.size()));
            }

            request.setProvinceCode(province == null ? null : province.getCode());
            request.setProvinceName(province == null ? null : province.getName());
            request.setCityCode(city == null ? null : city.getCode());
            request.setCityName(city == null ? null : city.getName());
            request.setCountyCode(county == null ? null : county.getCode());
            request.setCountyName(county == null ? null : county.getName());

            request.setDetailAddress(CollectionUtils.random(mockCommunityNames) + "小区"
                    + (random.nextInt(1) + 20) + "栋"
                    + (random.nextInt(1) + 16) + "单元");
            request.setDefaultAddress(false);

            mockAddresses.add(request);
        }

        return mockAddresses;
    }

    private List<AddCustomerInsuranceRequest> mockInsurances(List<String> companyDictValues) {

        List<AddCustomerInsuranceRequest> mockInsurances = new ArrayList<>();

        int count = (int) random.nextExponential() + 1;
        for (int i = 0; i < count; i++) {
            String companyDictValue = CollectionUtils.random(companyDictValues);
            String policyNumber = companyDictValue.toLowerCase() + i + System.currentTimeMillis();
            LocalDate duration = LocalDate.now().plusDays(1000 + random.nextInt(5000));
            String benefits = "福利：" + MockRandomPoetry.random();
            AddCustomerInsuranceRequest insurance = new AddCustomerInsuranceRequest();
            insurance.setCompanyDictValue(companyDictValue);
            insurance.setPolicyNumber(policyNumber);
            insurance.setDuration(duration);
            insurance.setBenefits(benefits);

            mockInsurances.add(insurance);
        }

        return mockInsurances;
    }

    private Set<String> mockCommunityNames() {
        Set<String> lastNames = Set.of("府", "第", "苑", "园", "宅", "城", "邸", "居", "庭");
        Set<String> firstNames = Set.of(
                "谐", "和", "绿", "满", "福", "海", "兰", "中", "圣", "菲", "悦", "富", "香", "翠", "御", "华", "盛",
                "蓝", "光", "季", "凌", "美", "嘉", "锦", "卓", "明", "珠", "瑞", "泰", "豪", "景", "维", "清");

        Set<String> communityNames = new HashSet<>();

        int total = 2000;
        for (int i = 0; i < total; i++) {
            int randomValue = random.nextInt(100);
            String communityName;
            if (randomValue < 30) {
                // 3个字
                communityName = CollectionUtils.random(firstNames)
                        + CollectionUtils.random(firstNames)
                        + CollectionUtils.random(lastNames);

            } else if (randomValue < 80) {
                // 4个字
                communityName = CollectionUtils.random(firstNames)
                        + CollectionUtils.random(firstNames)
                        + CollectionUtils.random(firstNames)
                        + CollectionUtils.random(lastNames);
            } else {
                // 5个字
                communityName = CollectionUtils.random(firstNames)
                        + CollectionUtils.random(firstNames)
                        + CollectionUtils.random(firstNames)
                        + CollectionUtils.random(firstNames)
                        + CollectionUtils.random(lastNames);
            }
            communityNames.add(communityName);
        }

        return communityNames;
    }

    @Test
    void list() {
        ListCustomerRequest request = new ListCustomerRequest();
        request.setConditionType(ListCustomerRequest.QueryConditionType.NAME);
//        request.setKeyword("迪亚波罗");
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

        List<CustomerSimpleVO> customers = customerService.listSimpleInfo(new ListCustomerRequest());
        for (CustomerSimpleVO customer : customers) {

            int tagCount = random.nextInt(customerTags.size());
            if (tagCount > 0) {
                Set<String> tagDictValues = Sets.newLinkedHashSet();
                for (int i = 0; i < tagCount; i++) {
                    String customerTag = CollectionUtils.random(customerTags);
                    tagDictValues.add(customerTag);
                }
                AddCustomerTagRequest addCustomerTagRequest = new AddCustomerTagRequest();
                addCustomerTagRequest.setCustomerCode(customer.getCode());
                addCustomerTagRequest.setTagDictValues(tagDictValues);

                super.performWhenCall(mockController.addCustomerTag(addCustomerTagRequest, super.mockAdmin()));
            }
        }

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

    @Test
    void removeCustomerTag() {
        String customerCode = "CU0000000001";
        String tagDictValue = "common_laborer";
        super.performWhenCall(mockController.removeCustomerTag(customerCode, tagDictValue));
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
