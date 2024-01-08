package com.tyrael.kharazim.application.customer.service;

import com.tyrael.kharazim.application.customer.vo.CustomerBaseVO;

/**
 * @author Tyrael Archangel
 * @since 2024/1/8
 */
public interface CustomerService {

    /**
     * 查询会员基本信息
     *
     * @param code 会员编码
     * @return 会员基本信息
     */
    CustomerBaseVO findByCode(String code);

}
