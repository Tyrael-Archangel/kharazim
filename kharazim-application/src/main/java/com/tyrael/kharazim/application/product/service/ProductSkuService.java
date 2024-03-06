package com.tyrael.kharazim.application.product.service;

import com.tyrael.kharazim.application.product.vo.sku.AddProductRequest;
import com.tyrael.kharazim.application.product.vo.sku.PageProductSkuRequest;
import com.tyrael.kharazim.application.product.vo.sku.ProductSkuVO;
import com.tyrael.kharazim.common.dto.PageResponse;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
public interface ProductSkuService {

    /**
     * get by code
     *
     * @param code code
     * @return 商品信息
     */
    ProductSkuVO getByCode(String code);

    /**
     * 创建SKU
     *
     * @param addRequest {@link AddProductRequest}
     * @return sku编码
     */
    String create(AddProductRequest addRequest);

    /**
     * SKU分页
     *
     * @param pageRequest {@link PageProductSkuRequest}
     * @return 商品分页数据
     */
    PageResponse<ProductSkuVO> page(PageProductSkuRequest pageRequest);

}
