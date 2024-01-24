package com.tyrael.kharazim.web.controller.customer;

import com.tyrael.kharazim.application.config.DictCodeConstants;
import com.tyrael.kharazim.application.customer.vo.communication.AddCustomerCommunicationLogRequest;
import com.tyrael.kharazim.application.customer.vo.communication.CustomerCommunicationLogPageRequest;
import com.tyrael.kharazim.application.system.dto.dict.SaveDictItemRequest;
import com.tyrael.kharazim.application.system.service.DictService;
import com.tyrael.kharazim.common.dto.Pair;
import com.tyrael.kharazim.common.dto.Pairs;
import com.tyrael.kharazim.common.util.CollectionUtils;
import com.tyrael.kharazim.common.util.RandomStringUtil;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
class CustomerCommunicationLogControllerTest extends BaseControllerTest<CustomerCommunicationLogController> {

    @Autowired
    private DictService dictService;

    CustomerCommunicationLogControllerTest() {
        super(CustomerCommunicationLogController.class);
    }

    @Test
    void page() {
        CustomerCommunicationLogPageRequest pageRequest = new CustomerCommunicationLogPageRequest();
        pageRequest.setCustomerCode("CU0000000001");
        super.performWhenCall(mockController.page(pageRequest));
    }

    @Test
    void add() {

        Set<String> customerCommunicationTypes = dictService.findEnabledItems(DictCodeConstants.CUSTOMER_COMMUNICATION_TYPE);
        if (customerCommunicationTypes.isEmpty()) {
            customerCommunicationTypes = addCustomerCommunicationTypeDict();
        }
        Set<String> customerCommunicationEvaluates = dictService.findEnabledItems(DictCodeConstants.CUSTOMER_COMMUNICATION_EVALUATE);
        if (customerCommunicationEvaluates.isEmpty()) {
            customerCommunicationEvaluates = addCustomerCommunicationEvaluateDict();
        }

        for (int i = 0; i < 5; i++) {
            AddCustomerCommunicationLogRequest addRequest = new AddCustomerCommunicationLogRequest();
            addRequest.setCustomerCode("CU0000000001");
            addRequest.setTypeDictValue(CollectionUtils.random(customerCommunicationTypes));
            addRequest.setContent("content--" + UUID.randomUUID() + "--" + RandomStringUtil.make(random.nextInt(300)));
            addRequest.setEvaluateDictValue(CollectionUtils.random(customerCommunicationEvaluates));
            super.performWhenCall(mockController.add(addRequest, super.mockAdmin()));
        }
    }

    private Set<String> addCustomerCommunicationTypeDict() {
        Pairs<String, String> customerTags = new Pairs<String, String>()
                .append("预约", "appointment")
                .append("处理投诉", "handle_complaint")
                .append("充值", "recharge")
                .append("回访", "revisit")
                .append("拜访", "visit");
        return addDictItems(DictCodeConstants.CUSTOMER_COMMUNICATION_TYPE.getDictCode(), customerTags);
    }

    private Set<String> addCustomerCommunicationEvaluateDict() {
        Pairs<String, String> customerTags = new Pairs<String, String>()
                .append("非常好", "perfect")
                .append("沟通愉快", "happy")
                .append("不错", "good")
                .append("沟通容易", "easy")
                .append("差", "bad");
        return addDictItems(DictCodeConstants.CUSTOMER_COMMUNICATION_EVALUATE.getDictCode(), customerTags);
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