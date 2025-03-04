package com.tyrael.kharazim.basicdata.controller.customer;

import com.tyrael.kharazim.base.dto.Pair;
import com.tyrael.kharazim.base.dto.Pairs;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.app.constant.BasicDataDictConstants;
import com.tyrael.kharazim.basicdata.app.dto.customer.communication.AddCustomerCommunicationLogRequest;
import com.tyrael.kharazim.basicdata.app.dto.customer.customer.CustomerSimpleVO;
import com.tyrael.kharazim.basicdata.app.dto.customer.customer.ListCustomerRequest;
import com.tyrael.kharazim.basicdata.app.service.customer.CustomerService;
import com.tyrael.kharazim.basicdata.sdk.model.DictItemVO;
import com.tyrael.kharazim.basicdata.sdk.service.DictServiceApi;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import com.tyrael.kharazim.test.mock.MockRandomPoetry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
public class AddCustomerCommunicationLogTest extends BaseControllerTest<CustomerCommunicationLogController> {

    @Autowired
    private DictServiceApi dictService;
    @Autowired
    private CustomerService customerService;

    AddCustomerCommunicationLogTest() {
        super(CustomerCommunicationLogController.class);
    }

    @Test
    void add() {

        Set<String> customerCommunicationTypes = dictService.dictItemKeys(BasicDataDictConstants.COMMUNICATION_TYPE);
        if (customerCommunicationTypes.isEmpty()) {
            customerCommunicationTypes = addCustomerCommunicationTypeDict();
        }
        Set<String> customerCommunicationEvaluates = dictService.dictItemKeys(BasicDataDictConstants.COMMUNICATION_EVALUATE);
        if (customerCommunicationEvaluates.isEmpty()) {
            customerCommunicationEvaluates = addCustomerCommunicationEvaluateDict();
        }

        List<CustomerSimpleVO> customers = customerService.listSimpleInfo(new ListCustomerRequest());
        for (CustomerSimpleVO customer : customers) {
            int logCount = random.nextInt(50) - 10;
            for (int i = 0; i < logCount; i++) {
                AddCustomerCommunicationLogRequest addRequest = new AddCustomerCommunicationLogRequest();
                addRequest.setCustomerCode(customer.getCode());
                addRequest.setServiceUserCode(super.mockAdmin().getCode());
                addRequest.setTypeDictKey(CollectionUtils.random(customerCommunicationTypes));
                addRequest.setEvaluateDictKey(CollectionUtils.random(customerCommunicationEvaluates));
                addRequest.setContent(MockRandomPoetry.random());
                addRequest.setCommunicationTime(LocalDateTime.now()
                        .minusHours(random.nextInt(300))
                        .minusSeconds(random.nextInt(1000)));
                super.performWhenCall(mockController.add(addRequest, super.mockAdmin()));
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
        return addDictItems(BasicDataDictConstants.COMMUNICATION_TYPE.getCode(), customerTags);
    }

    private Set<String> addCustomerCommunicationEvaluateDict() {
        Pairs<String, String> customerTags = new Pairs<String, String>()
                .append("perfect", "非常好")
                .append("happy", "沟通愉快")
                .append("good", "不错")
                .append("easy", "沟通容易")
                .append("bad", "差");
        return addDictItems(BasicDataDictConstants.COMMUNICATION_EVALUATE.getCode(), customerTags);
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