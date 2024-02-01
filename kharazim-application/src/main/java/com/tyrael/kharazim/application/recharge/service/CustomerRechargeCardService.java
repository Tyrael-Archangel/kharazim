package com.tyrael.kharazim.application.recharge.service;

import com.tyrael.kharazim.application.recharge.vo.CustomerRechargeRequest;

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

}
