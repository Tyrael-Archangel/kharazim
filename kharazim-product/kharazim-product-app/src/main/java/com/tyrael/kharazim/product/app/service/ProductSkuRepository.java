package com.tyrael.kharazim.product.app.service;


import com.tyrael.kharazim.product.app.vo.sku.ProductSkuVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/3/28
 */
public interface ProductSkuRepository {

    /**
     * list sku info by codes
     *
     * @param skuCodes SKU codes
     * @return sku info
     */
    List<ProductSkuVO> listByCodes(Collection<String> skuCodes);

    /**
     * map sku info by codes
     *
     * @param skuCodes SKU codes
     * @return sku info map
     */
    default Map<String, ProductSkuVO> mapByCodes(Collection<String> skuCodes) {
        return this.listByCodes(skuCodes)
                .stream()
                .collect(Collectors.toMap(ProductSkuVO::getCode, e -> e));
    }

}
