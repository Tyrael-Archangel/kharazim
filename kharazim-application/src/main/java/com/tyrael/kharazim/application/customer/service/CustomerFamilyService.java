package com.tyrael.kharazim.application.customer.service;

import com.tyrael.kharazim.application.customer.vo.family.CustomerFamilyVO;

/**
 * @author Tyrael Archangel
 * @since 2024/1/19
 */
public interface CustomerFamilyService {

    /**
     * 查询家庭信息
     *
     * @param familyCode 家庭编码
     * @return 家庭信息
     */
    CustomerFamilyVO family(String familyCode);

}
