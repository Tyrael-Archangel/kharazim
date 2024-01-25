package com.tyrael.kharazim.application.recharge.service;

import com.tyrael.kharazim.application.recharge.vo.AddRechargeCardTypeRequest;

/**
 * @author Tyrael Archangel
 * @since 2024/1/25
 */
public interface RechargeCardTypeService {

    /**
     * 新建储值卡项
     *
     * @param addRequest {@link AddRechargeCardTypeRequest}
     * @return 储值卡项编码
     */
    String create(AddRechargeCardTypeRequest addRequest);

}
