package com.tyrael.kharazim.application.product.service;

import com.tyrael.kharazim.application.product.vo.spu.AddProductSpuRequest;
import com.tyrael.kharazim.application.product.vo.spu.PageProductSpuRequest;
import com.tyrael.kharazim.application.product.vo.spu.ProductSpuVO;
import com.tyrael.kharazim.common.dto.PageResponse;

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

    /**
     * 创建SPU
     *
     * @param addRequest {@link AddProductSpuRequest}
     * @return spu编码
     */
    String create(AddProductSpuRequest addRequest);

    /**
     * SPU分页
     *
     * @param pageRequest {@link PageProductSpuRequest}
     * @return SPU分页数据
     */
    PageResponse<ProductSpuVO> page(PageProductSpuRequest pageRequest);

}
