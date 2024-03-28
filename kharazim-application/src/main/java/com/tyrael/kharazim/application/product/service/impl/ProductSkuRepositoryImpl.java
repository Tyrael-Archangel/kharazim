package com.tyrael.kharazim.application.product.service.impl;

import com.tyrael.kharazim.application.product.converter.ProductSkuConverter;
import com.tyrael.kharazim.application.product.domain.ProductSku;
import com.tyrael.kharazim.application.product.mapper.ProductSkuMapper;
import com.tyrael.kharazim.application.product.service.ProductSkuRepository;
import com.tyrael.kharazim.application.product.vo.sku.ProductSkuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/3/28
 */
@Service
@RequiredArgsConstructor
public class ProductSkuRepositoryImpl implements ProductSkuRepository {

    private final ProductSkuConverter productSkuConverter;
    private final ProductSkuMapper productSkuMapper;

    @Override
    public List<ProductSkuVO> listByCodes(Collection<String> skuCodes) {
        if (skuCodes == null || skuCodes.isEmpty()) {
            return new ArrayList<>();
        }
        List<ProductSku> productSkus = productSkuMapper.listByCodes(skuCodes);
        return productSkuConverter.skuVOs(productSkus);
    }

}
