package com.tyrael.kharazim.application.customer.mapper;

import com.tyrael.kharazim.application.base.BasePageMapper;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.customer.domain.CustomerCommunicationLog;
import com.tyrael.kharazim.application.customer.vo.communication.CustomerCommunicationLogPageRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
@Mapper
public interface CustomerCommunicationLogMapper extends BasePageMapper<CustomerCommunicationLog> {

    /**
     * page
     *
     * @param pageRequest {@link CustomerCommunicationLogPageRequest}
     * @return CommunicationLogPageResponse
     */
    default PageResponse<CustomerCommunicationLog> page(CustomerCommunicationLogPageRequest pageRequest) {
        LambdaQueryWrapperX<CustomerCommunicationLog> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfHasText(CustomerCommunicationLog::getCustomerCode, pageRequest.getCustomerCode())
                .eqIfHasText(CustomerCommunicationLog::getServiceUserCode, pageRequest.getServiceUserCode())
                .eqIfHasText(CustomerCommunicationLog::getTypeDict, pageRequest.getTypeDictKey())
                .eqIfHasText(CustomerCommunicationLog::getEvaluateDict, pageRequest.getEvaluateDictKey())
                .geIfPresent(CustomerCommunicationLog::getCreateTime, pageRequest.getCreatedBegin())
                .leIfPresent(CustomerCommunicationLog::getCreateTime, pageRequest.getCreatedEnd())
                .geIfPresent(CustomerCommunicationLog::getCommunicationTime, pageRequest.getCommunicationBegin())
                .leIfPresent(CustomerCommunicationLog::getCommunicationTime, pageRequest.getCommunicationEnd());

        queryWrapper.orderByDesc(CustomerCommunicationLog::getCreateTime);
        queryWrapper.orderByDesc(CustomerCommunicationLog::getId);

        return page(pageRequest, queryWrapper);
    }

}
