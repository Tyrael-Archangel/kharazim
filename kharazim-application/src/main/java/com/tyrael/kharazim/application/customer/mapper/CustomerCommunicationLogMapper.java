package com.tyrael.kharazim.application.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public interface CustomerCommunicationLogMapper extends BaseMapper<CustomerCommunicationLog> {

    /**
     * page
     *
     * @param pageRequest {@link CustomerCommunicationLogPageRequest}
     * @return CommunicationLogPageResponse
     */
    default PageResponse<CustomerCommunicationLog> page(CustomerCommunicationLogPageRequest pageRequest) {
        LambdaQueryWrapperX<CustomerCommunicationLog> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfHasText(CustomerCommunicationLog::getCustomerCode, pageRequest.getCustomerCode())
                .eqIfHasText(CustomerCommunicationLog::getServiceUserCode, pageRequest.getServiceUserCode());

        queryWrapper.orderByDesc(CustomerCommunicationLog::getCreateTime);
        queryWrapper.orderByDesc(CustomerCommunicationLog::getId);

        Page<CustomerCommunicationLog> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<CustomerCommunicationLog> communicationLogPage = selectPage(page, queryWrapper);
        return PageResponse.success(communicationLogPage.getRecords(),
                communicationLogPage.getTotal(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
    }

}
