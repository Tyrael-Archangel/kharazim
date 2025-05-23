package com.tyrael.kharazim.basicdata.controller.customer;

import com.tyrael.kharazim.base.dto.Pair;
import com.tyrael.kharazim.base.dto.Pairs;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.DubboReferenceHolder;
import com.tyrael.kharazim.basicdata.app.constant.BasicDataDictConstants;
import com.tyrael.kharazim.basicdata.app.dto.address.AddressTreeNodeDTO;
import com.tyrael.kharazim.basicdata.app.dto.customer.customer.*;
import com.tyrael.kharazim.basicdata.app.enums.CustomerCertificateTypeEnum;
import com.tyrael.kharazim.basicdata.app.enums.CustomerGenderEnum;
import com.tyrael.kharazim.basicdata.app.service.address.AddressQueryService;
import com.tyrael.kharazim.basicdata.app.service.customer.CustomerService;
import com.tyrael.kharazim.basicdata.sdk.model.DictItemVO;
import com.tyrael.kharazim.basicdata.sdk.service.DictServiceApi;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import com.tyrael.kharazim.test.mock.MockRandomPoetry;
import com.tyrael.kharazim.user.sdk.model.UserSimpleVO;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
@SpringBootTest
public class AddCustomerTest extends BaseControllerTest<CustomerController> {

    @Autowired
    private AddressQueryService addressQueryService;

    @Autowired
    private DictServiceApi dictService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

    public AddCustomerTest() {
        super(CustomerController.class);
    }

    @Test
    @Order(1)
    void add() {

        List<AddressTreeNodeDTO> addressTree = addressQueryService.fullTree();
        Pairs<String, CustomerGenderEnum> mockCustomers = this.mockCustomers();
        Set<String> mockCommunityNames = this.mockCommunityNames();
        List<String> serviceUserCodes = dubboReferenceHolder.userServiceApi.listAll()
                .stream()
                .map(UserSimpleVO::getCode)
                .toList();
        Set<String> companyItems = dictService.dictItemKeys(BasicDataDictConstants.INSURANCE_COMPANY);
        if (companyItems.isEmpty()) {
            companyItems = addInsuranceCompanyDict();
        }
        List<String> companyItemValues = new ArrayList<>(companyItems);

        Set<String> customerSourceChannels = dictService.dictItemKeys(BasicDataDictConstants.CUSTOMER_SOURCE_CHANNEL);
        if (customerSourceChannels.isEmpty()) {
            customerSourceChannels = addCustomerSourceChannelDict();
        }
        List<String> customerSourceChannelValues = new ArrayList<>(customerSourceChannels);

        for (Pair<String, CustomerGenderEnum> userNameAndGender : mockCustomers) {

            String name = userNameAndGender.left();
            CustomerGenderEnum gender = userNameAndGender.right();
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
            addCustomerRequest.setCertificateType(CustomerCertificateTypeEnum.ID_CARD);
            addCustomerRequest.setCertificateCode("510123111122334444");
            addCustomerRequest.setSourceChannelDictKey(CollectionUtils.random(customerSourceChannelValues));
            addCustomerRequest.setRemark(MockRandomPoetry.random());
            addCustomerRequest.setServiceUserCode(CollectionUtils.random(serviceUserCodes));
            addCustomerRequest.setSalesConsultantCode(CollectionUtils.random(serviceUserCodes));
            addCustomerRequest.setCustomerAddresses(this.mockAddress(name, phone, addressTree, mockCommunityNames));
            addCustomerRequest.setCustomerInsurances(this.mockInsurances(companyItemValues));
            super.performWhenCall(mockController.add(addCustomerRequest, dubboReferenceHolder.userServiceApi.mock()));
        }

    }

    private Pairs<String, CustomerGenderEnum> mockCustomers() {
        Set<String> familyNames = familyNames();
        List<String> boyNames = new ArrayList<>(popularBoyNames());
        List<String> girlNames = new ArrayList<>(popularGirlNames());
        int totalUserCount = random.nextInt(400) + 400;

        Pairs<String, CustomerGenderEnum> mockedUsers = new Pairs<>();
        Set<String> fullNames = new HashSet<>();
        for (int i = 0; i < totalUserCount; i++) {
            boolean boy = random.nextBoolean();
            List<String> nameWords = boy ? boyNames : girlNames;
            CustomerGenderEnum gender = boy ? CustomerGenderEnum.MALE : CustomerGenderEnum.FEMALE;

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


            String detailAddress = CollectionUtils.random(mockCommunityNames) + "小区"
                    + (random.nextInt(20) + 1) + "栋"
                    + (random.nextInt(16) + 1) + "单元";
            if (random.nextInt(100) > 40) {
                detailAddress += ((random.nextInt((random.nextInt(32) + 1)) + 1)
                        + "0" + (random.nextInt(random.nextInt(9) + 1) + 1));
            }
            request.setDetailAddress(detailAddress);
            request.setDefaultAddress(false);

            mockAddresses.add(request);
        }

        return mockAddresses;
    }

    private List<AddCustomerInsuranceRequest> mockInsurances(List<String> companyDictKeys) {

        List<AddCustomerInsuranceRequest> mockInsurances = new ArrayList<>();

        int count = (int) random.nextExponential() + 1;
        for (int i = 0; i < count; i++) {
            String companyDictKey = CollectionUtils.random(companyDictKeys);
            String policyNumber = companyDictKey.toLowerCase() + i + System.currentTimeMillis();
            LocalDate duration = LocalDate.now().plusDays(1000 + random.nextInt(5000));
            String benefits = "福利：" + MockRandomPoetry.random();
            AddCustomerInsuranceRequest insurance = new AddCustomerInsuranceRequest();
            insurance.setCompanyDictKey(companyDictKey);
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

    private Set<String> addInsuranceCompanyDict() {

        Pairs<String, String> insuranceCompanies = new Pairs<String, String>()
                .append("AIA", "AIA | 友邦")
                .append("ALLIANZ", "Allianz | 安联")
                .append("AXA", "AXA | 安盛")
                .append("BUPA", "Bupa | 保柏")
                .append("CIGNA", "Cigna | 信诺")
                .append("CIGNACMB", "Cigna & CMB | 招商信诺")
                .append("MSH", "MSH | 万欣和")
                .append("PINGAN", "PINGAN | 中国平安");

        return addDictItems(BasicDataDictConstants.INSURANCE_COMPANY.getCode(), insuranceCompanies);
    }

    private Set<String> addCustomerSourceChannelDict() {
        Pairs<String, String> customerSourceChannels = new Pairs<String, String>()
                .append("OFFLINE", "线下")
                .append("MEI_TUAN", "美团")
                .append("BAI_DU", "百度")
                .append("TIK_TOK", "抖音")
                .append("WE_CHAT", "微信")
                .append("OTHER", "其他");

        return addDictItems(BasicDataDictConstants.CUSTOMER_SOURCE_CHANNEL.getCode(), customerSourceChannels);
    }

    @Test
    @Order(2)
    void addCustomerTag() {
        Set<String> customerTagDictItems = dictService.dictItemKeys(BasicDataDictConstants.CUSTOMER_TAG);
        if (customerTagDictItems.isEmpty()) {
            customerTagDictItems = addCustomerTagDict();
        }

        List<String> customerTags = new ArrayList<>(customerTagDictItems);

        List<CustomerSimpleVO> customers = customerService.listSimpleInfo(new ListCustomerRequest());
        for (CustomerSimpleVO customer : customers) {
            Set<String> tagDictKeys = new LinkedHashSet<>(CollectionUtils.randomSubList(customerTags, 20));
            if (random.nextInt(100) > 50 && !tagDictKeys.isEmpty()) {
                AddCustomerTagRequest addCustomerTagRequest = new AddCustomerTagRequest();
                addCustomerTagRequest.setCustomerCode(customer.getCode());
                addCustomerTagRequest.setTagDictKeys(tagDictKeys);
                super.performWhenCall(mockController.addCustomerTag(addCustomerTagRequest, dubboReferenceHolder.userServiceApi.mock()));
            }
        }
    }

    private Set<String> addCustomerTagDict() {
        Pairs<String, String> customerTags = new Pairs<String, String>()
                .append("bronze", "青铜")
                .append("silver", "白银")
                .append("gold", "黄金")
                .append("platinum", "铂金")
                .append("diamond", "钻石")
                .append("fitness_enthusiasts", "健身爱好者")
                .append("pay_attention_to_maintenance", "注重保养")
                .append("hot_figure", "身材火辣")
                .append("high_value_sensitive", "高价值敏感")
                .append("good_credit", "信用良好")
                .append("southerner", "南方人")
                .append("northerner", "北方人")
                .append("foreigner", "外国人")
                .append("enterprise_senior_manager", "企业高管")
                .append("common_laborer", "普通工人")
                .append("housewife", "家庭妇女")
                .append("retiree", "退休人员")
                .append("strong_spending_power", "消费能力强");
        return addDictItems(BasicDataDictConstants.CUSTOMER_TAG.getCode(), customerTags);
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
            dictService.addDictItem(addDictItemRequest);
            dictItemKeys.add(dictItem.left());
        }
        return dictItemKeys;
    }

}
