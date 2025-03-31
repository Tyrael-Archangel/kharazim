package com.tyrael.kharazim.product.sdk.service;

import com.tyrael.kharazim.product.sdk.model.ProductSkuVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/3/25
 */
public interface ProductServiceApi {

    /**
     * list all product
     *
     * @return products
     */
    List<ProductSkuVO> listAll();

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

    /**
     * filter sku codes by name
     *
     * @param skuName skuName
     * @return skuCodes
     */
    List<String> filterByName(String skuName);

}
