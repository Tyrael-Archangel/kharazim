package com.tyrael.kharazim.basicdata.app.service.customer;

import com.tyrael.kharazim.authentication.Principal;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.basicdata.app.dto.customer.communication.AddCustomerCommunicationLogRequest;
import com.tyrael.kharazim.basicdata.app.dto.customer.communication.CustomerCommunicationLogPageRequest;
import com.tyrael.kharazim.basicdata.app.dto.customer.communication.CustomerCommunicationLogVO;

/**
 * @author Tyrael Archangel
 * @since 2024/1/24
 */
public interface CustomerCommunicationLogService {

    /**
     * 会员沟通记录分页查询
     *
     * @param pageRequest {@link CustomerCommunicationLogPageRequest}
     * @return 会员沟通记录分页数据
     */
    PageResponse<CustomerCommunicationLogVO> page(CustomerCommunicationLogPageRequest pageRequest);

    /**
     * 新建沟通记录
     *
     * @param addRequest  {@link AddCustomerCommunicationLogRequest}
     * @param currentUser 操作客服
     * @return 返回沟通记录ID
     */
    Long add(AddCustomerCommunicationLogRequest addRequest, Principal currentUser);

}
