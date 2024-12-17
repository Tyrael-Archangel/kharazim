package com.tyrael.kharazim.web.controller.customer;

import com.tyrael.kharazim.application.customer.service.CustomerService;
import com.tyrael.kharazim.application.customer.vo.communication.AddCustomerCommunicationLogRequest;
import com.tyrael.kharazim.application.customer.vo.customer.CustomerSimpleVO;
import com.tyrael.kharazim.application.customer.vo.customer.ListCustomerRequest;
import com.tyrael.kharazim.application.system.domain.DictConstants;
import com.tyrael.kharazim.application.system.dto.dict.SaveDictItemRequest;
import com.tyrael.kharazim.application.system.service.DictService;
import com.tyrael.kharazim.common.dto.Pair;
import com.tyrael.kharazim.common.dto.Pairs;
import com.tyrael.kharazim.common.util.CollectionUtils;
import com.tyrael.kharazim.mock.MockRandomPoetry;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
public class AddCustomerCommunicationLogTest extends BaseControllerTest<CustomerCommunicationLogController> {

    @Autowired
    private DictService dictService;
    @Autowired
    private CustomerService customerService;

    AddCustomerCommunicationLogTest() {
        super(CustomerCommunicationLogController.class);
    }

    @Test
    void add() {

        Set<String> customerCommunicationTypes = dictService.dictItemKeys(DictConstants.COMMUNICATION_TYPE);
        if (customerCommunicationTypes.isEmpty()) {
            customerCommunicationTypes = addCustomerCommunicationTypeDict();
        }
        Set<String> customerCommunicationEvaluates = dictService.dictItemKeys(DictConstants.COMMUNICATION_EVALUATE);
        if (customerCommunicationEvaluates.isEmpty()) {
            customerCommunicationEvaluates = addCustomerCommunicationEvaluateDict();
        }

        List<CustomerSimpleVO> customers = customerService.listSimpleInfo(new ListCustomerRequest());
        for (CustomerSimpleVO customer : customers) {
            int logCount = random.nextInt(50) - 10;
            for (int i = 0; i < logCount; i++) {
                AddCustomerCommunicationLogRequest addRequest = new AddCustomerCommunicationLogRequest();
                addRequest.setCustomerCode(customer.getCode());
                addRequest.setTypeDictKey(CollectionUtils.random(customerCommunicationTypes));
                addRequest.setContent(MockRandomPoetry.random());
                addRequest.setEvaluateDictKey(CollectionUtils.random(customerCommunicationEvaluates));
                super.performWhenCall(mockController.add(addRequest, super.mockUser()));
            }
        }

    }

    private Set<String> addCustomerCommunicationTypeDict() {
        Pairs<String, String> customerTags = new Pairs<String, String>()
                .append("appointment", "预约")
                .append("handle_complaint", "处理投诉")
                .append("recharge", "充值")
                .append("revisit", "回访")
                .append("visit", "拜访");
        return addDictItems(DictConstants.COMMUNICATION_TYPE.getCode(), customerTags);
    }

    private Set<String> addCustomerCommunicationEvaluateDict() {
        Pairs<String, String> customerTags = new Pairs<String, String>()
                .append("perfect", "非常好")
                .append("happy", "沟通愉快")
                .append("good", "不错")
                .append("easy", "沟通容易")
                .append("bad", "差");
        return addDictItems(DictConstants.COMMUNICATION_EVALUATE.getCode(), customerTags);
    }

    private Set<String> addDictItems(String dictCode, Pairs<String, String> dictItems) {
        Set<String> dictItemKeys = new LinkedHashSet<>();
        for (int i = 0; i < dictItems.size(); i++) {
            Pair<String, String> dictItem = dictItems.get(i);
            SaveDictItemRequest addDictItemRequest = new SaveDictItemRequest();
            addDictItemRequest.setDictCode(dictCode);
            addDictItemRequest.setKey(dictItem.left());
            addDictItemRequest.setValue(dictItem.right());
            addDictItemRequest.setSort(i + 1);
            dictService.addDictItem(addDictItemRequest, super.mockUser());
            dictItemKeys.add(dictItem.left());
        }
        return dictItemKeys;
    }

}