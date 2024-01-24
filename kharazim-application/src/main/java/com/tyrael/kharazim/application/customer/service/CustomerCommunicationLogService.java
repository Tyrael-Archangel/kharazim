package com.tyrael.kharazim.application.customer.service;

import com.tyrael.kharazim.application.customer.vo.communication.CustomerCommunicationLogPageRequest;
import com.tyrael.kharazim.application.customer.vo.communication.CustomerCommunicationLogVO;
import com.tyrael.kharazim.common.dto.PageResponse;

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


}
