package com.tyrael.kharazim.application.product.service;

import com.tyrael.kharazim.application.product.vo.spu.ProductSpuVO;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
public interface ProductSpuService {

    /**
     * get by code
     *
     * @param code code
     * @return SPU信息
     */
    ProductSpuVO getByCode(String code);

}
