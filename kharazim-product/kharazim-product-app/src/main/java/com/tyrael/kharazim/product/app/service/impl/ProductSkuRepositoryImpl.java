package com.tyrael.kharazim.product.app.service.impl;

import com.tyrael.kharazim.product.app.converter.ProductSkuConverter;
import com.tyrael.kharazim.product.app.domain.ProductSku;
import com.tyrael.kharazim.product.app.mapper.ProductSkuMapper;
import com.tyrael.kharazim.product.app.service.ProductSkuRepository;
import com.tyrael.kharazim.product.app.vo.sku.ProductSkuDTO;
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
    public List<ProductSkuDTO> listAll() {
        List<ProductSku> productSkus = productSkuMapper.listAll();
        return productSkuConverter.skuVOs(productSkus);
    }

    @Override
    public List<ProductSkuDTO> listByCodes(Collection<String> skuCodes) {
        if (skuCodes == null || skuCodes.isEmpty()) {
            return new ArrayList<>();
        }
        List<ProductSku> productSkus = productSkuMapper.listByCodes(skuCodes);
        return productSkuConverter.skuVOs(productSkus);
    }

    @Override
    public List<String> filterCodesByName(String skuName) {
        return productSkuMapper.filterCodesByName(skuName);
    }

}
