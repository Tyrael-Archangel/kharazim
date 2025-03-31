package com.tyrael.kharazim.product.app.service;


import com.tyrael.kharazim.product.app.vo.sku.ProductSkuDTO;

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
     * all products
     *
     * @return products
     */
    List<ProductSkuDTO> listAll();

    /**
     * list sku info by codes
     *
     * @param skuCodes SKU codes
     * @return sku info
     */
    List<ProductSkuDTO> listByCodes(Collection<String> skuCodes);

    /**
     * map sku info by codes
     *
     * @param skuCodes SKU codes
     * @return sku info map
     */
    default Map<String, ProductSkuDTO> mapByCodes(Collection<String> skuCodes) {
        return this.listByCodes(skuCodes)
                .stream()
                .collect(Collectors.toMap(ProductSkuDTO::getCode, e -> e));
    }

    /**
     * filter sku codes by name
     *
     * @param skuName skuName
     * @return skuCodes
     */
    List<String> filterCodesByName(String skuName);

}
