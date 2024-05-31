package com.tyrael.kharazim.application.customer.service.impl;

import com.google.common.collect.Sets;
import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.config.DictCodeConstants;
import com.tyrael.kharazim.application.customer.domain.Customer;
import com.tyrael.kharazim.application.customer.domain.CustomerCommunicationLog;
import com.tyrael.kharazim.application.customer.mapper.CustomerCommunicationLogMapper;
import com.tyrael.kharazim.application.customer.mapper.CustomerMapper;
import com.tyrael.kharazim.application.customer.service.CustomerCommunicationLogService;
import com.tyrael.kharazim.application.customer.vo.communication.AddCustomerCommunicationLogRequest;
import com.tyrael.kharazim.application.customer.vo.communication.CustomerCommunicationLogPageRequest;
import com.tyrael.kharazim.application.customer.vo.communication.CustomerCommunicationLogVO;
import com.tyrael.kharazim.application.system.service.DictService;
import com.tyrael.kharazim.application.user.domain.User;
import com.tyrael.kharazim.application.user.mapper.UserMapper;
import com.tyrael.kharazim.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
@Service
@RequiredArgsConstructor
public class CustomerCommunicationLogServiceImpl implements CustomerCommunicationLogService {

    private final CustomerCommunicationLogMapper customerCommunicationLogMapper;
    private final CustomerMapper customerMapper;
    private final UserMapper userMapper;
    private final DictService dictService;

    @Override
    public PageResponse<CustomerCommunicationLogVO> page(CustomerCommunicationLogPageRequest pageRequest) {
        PageResponse<CustomerCommunicationLog> pageResponse = customerCommunicationLogMapper.page(pageRequest);
        Collection<CustomerCommunicationLog> pageLogs = pageResponse.getData();

        Set<String> customerCodes = Sets.newHashSet();
        Set<String> serviceUserCodes = Sets.newHashSet();
        for (CustomerCommunicationLog customerCommunicationLog : pageLogs) {
            customerCodes.add(customerCommunicationLog.getCustomerCode());
            serviceUserCodes.add(customerCommunicationLog.getServiceUserCode());
        }

        Map<String, User> userMap = userMapper.mapByCodes(serviceUserCodes);
        Map<String, Customer> customerMap = customerMapper.mapByCodes(customerCodes);

        Map<String, String> typeDictItemMap = dictService.dictItemMap(DictCodeConstants.CUSTOMER_COMMUNICATION_TYPE);
        Map<String, String> evaluateDictItemMap = dictService.dictItemMap(DictCodeConstants.CUSTOMER_COMMUNICATION_EVALUATE);

        List<CustomerCommunicationLogVO> logs = pageLogs.stream()
                .map(e -> {
                    User user = userMap.get(e.getServiceUserCode());
                    Customer customer = customerMap.get(e.getCustomerCode());
                    return this.communicationLogVO(e, user, customer, typeDictItemMap, evaluateDictItemMap);
                })
                .collect(Collectors.toList());
        return PageResponse.success(logs, pageResponse.getTotalCount(), pageResponse.getPageSize(), pageResponse.getPageNum());
    }

    private CustomerCommunicationLogVO communicationLogVO(CustomerCommunicationLog customerCommunicationLog,
                                                          User user,
                                                          Customer customer,
                                                          Map<String, String> typeDictItemMap,
                                                          Map<String, String> evaluateDictItemMap) {
        return CustomerCommunicationLogVO.builder()
                .type(typeDictItemMap.get(customerCommunicationLog.getTypeDict()))
                .typeDictValue(customerCommunicationLog.getTypeDict())
                .customerCode(customerCommunicationLog.getCustomerCode())
                .customerName(customer == null ? null : customer.getName())
                .serviceUserCode(customerCommunicationLog.getServiceUserCode())
                .serviceUserName(user == null ? null : user.getNickName())
                .content(customerCommunicationLog.getContent())
                .evaluateDictValue(customerCommunicationLog.getEvaluateDict())
                .evaluate(evaluateDictItemMap.get(customerCommunicationLog.getEvaluateDict()))
                .communicationTime(customerCommunicationLog.getCommunicationTime())
                .creator(customerCommunicationLog.getCreator())
                .createTime(customerCommunicationLog.getCreateTime())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(AddCustomerCommunicationLogRequest addRequest, AuthUser currentUser) {
        customerMapper.ensureCustomerExist(addRequest.getCustomerCode());
        dictService.ensureDictItemEnable(DictCodeConstants.CUSTOMER_COMMUNICATION_TYPE, addRequest.getTypeDictValue());
        dictService.ensureDictItemEnable(DictCodeConstants.CUSTOMER_COMMUNICATION_EVALUATE, addRequest.getEvaluateDictValue());

        CustomerCommunicationLog customerCommunicationLog = new CustomerCommunicationLog();
        customerCommunicationLog.setTypeDict(addRequest.getTypeDictValue());
        customerCommunicationLog.setCustomerCode(addRequest.getCustomerCode());
        customerCommunicationLog.setServiceUserCode(currentUser.getCode());
        customerCommunicationLog.setContent(addRequest.getContent());
        customerCommunicationLog.setEvaluateDict(addRequest.getEvaluateDictValue());
        customerCommunicationLog.setCommunicationTime(LocalDateTime.now());
        customerCommunicationLog.setCreator(currentUser.getNickName());
        customerCommunicationLog.setCreatorCode(currentUser.getCode());
        customerCommunicationLog.setCreateTime(LocalDateTime.now());

        customerCommunicationLogMapper.insert(customerCommunicationLog);
        return customerCommunicationLog.getId();
    }

}
