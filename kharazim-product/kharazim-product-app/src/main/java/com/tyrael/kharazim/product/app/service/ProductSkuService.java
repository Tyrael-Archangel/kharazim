package com.tyrael.kharazim.product.app.service;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.product.app.vo.sku.AddProductRequest;
import com.tyrael.kharazim.product.app.vo.sku.PageProductSkuRequest;
import com.tyrael.kharazim.product.app.vo.sku.ProductSkuDTO;

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
    ProductSkuDTO getByCode(String code);

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
    PageResponse<ProductSkuDTO> page(PageProductSkuRequest pageRequest);

}
