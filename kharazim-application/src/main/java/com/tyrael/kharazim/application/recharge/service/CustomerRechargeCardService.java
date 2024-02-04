package com.tyrael.kharazim.application.recharge.service;

import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeCardPageRequest;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeCardVO;
import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeRequest;
import com.tyrael.kharazim.common.dto.PageResponse;

/**
 * @author Tyrael Archangel
 * @since 2024/2/1
 */
public interface CustomerRechargeCardService {

    /**
     * 充值
     *
     * @param rechargeRequest {@link CustomerRechargeRequest}
     */
    void recharge(CustomerRechargeRequest rechargeRequest);

    /**
     * 会员储值单分页
     *
     * @param pageRequest {@link CustomerRechargeCardPageRequest}
     * @return 会员储值单分页数据
     */
    PageResponse<CustomerRechargeCardVO> page(CustomerRechargeCardPageRequest pageRequest);

}
