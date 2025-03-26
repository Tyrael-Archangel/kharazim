package com.tyrael.kharazim.product.provider;

import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.product.app.service.ProductSkuRepository;
import com.tyrael.kharazim.product.app.vo.sku.ProductSkuDTO;
import com.tyrael.kharazim.product.sdk.model.ProductSkuVO;
import com.tyrael.kharazim.product.sdk.service.ProductServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/3/25
 */
@Component
@DubboService
@RequiredArgsConstructor
public class ProductServiceProvider implements ProductServiceApi {

    private final ProductSkuRepository productSkuRepository;

    @Override
    public List<ProductSkuVO> listAll() {
        List<ProductSkuDTO> products = productSkuRepository.listAll();
        return products.stream()
                .map(this::convertProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductSkuVO> listByCodes(Collection<String> skuCodes) {
        List<ProductSkuDTO> products = productSkuRepository.listByCodes(skuCodes);
        return products.stream()
                .map(this::convertProduct)
                .collect(Collectors.toList());
    }

    private ProductSkuVO convertProduct(ProductSkuDTO product) {
        List<ProductSkuVO.AttributeVO> attributes = CollectionUtils.safeStream(product.getAttributes())
                .map(e -> new ProductSkuVO.AttributeVO(e.getName(), e.getValue()))
                .collect(Collectors.toList());
        return new ProductSkuVO()
                .setCode(product.getCode())
                .setName(product.getName())
                .setCategoryCode(product.getCategoryCode())
                .setCategoryName(product.getCategoryName())
                .setCategoryFullName(product.getCategoryFullName())
                .setSupplierCode(product.getSupplierCode())
                .setSupplierName(product.getSupplierName())
                .setUnitCode(product.getUnitCode())
                .setUnitName(product.getUnitName())
                .setDefaultImage(product.getDefaultImage())
                .setImages(product.getImages())
                .setDescription(product.getDescription())
                .setAttributes(attributes)
                .setAttributesDesc(product.getAttributesDesc());
    }

}
