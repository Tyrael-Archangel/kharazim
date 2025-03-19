package com.tyrael.kharazim.product.app.service.impl;

import com.tyrael.kharazim.product.app.converter.ProductSkuConverter;
import com.tyrael.kharazim.product.app.domain.ProductSku;
import com.tyrael.kharazim.product.app.mapper.ProductSkuMapper;
import com.tyrael.kharazim.product.app.service.ProductSkuRepository;
import com.tyrael.kharazim.product.app.vo.sku.ProductSkuVO;
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
