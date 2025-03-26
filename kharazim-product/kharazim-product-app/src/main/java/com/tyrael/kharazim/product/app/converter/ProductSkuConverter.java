package com.tyrael.kharazim.product.app.converter;

import com.tyrael.kharazim.product.app.domain.ProductSku;
import com.tyrael.kharazim.product.app.domain.ProductUnitDO;
import com.tyrael.kharazim.product.app.mapper.ProductUnitMapper;
import com.tyrael.kharazim.product.app.service.ProductCategoryService;
import com.tyrael.kharazim.product.app.vo.category.ProductCategoryVO;
import com.tyrael.kharazim.product.app.vo.sku.Attribute;
import com.tyrael.kharazim.product.app.vo.sku.ProductSkuDTO;
import com.tyrael.kharazim.purchase.sdk.model.SupplierVO;
import com.tyrael.kharazim.purchase.sdk.service.SupplierServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
@Component
@RequiredArgsConstructor
public class ProductSkuConverter {

    private final ProductCategoryService productCategoryService;
    private final ProductUnitMapper unitMapper;

    @DubboReference
    private SupplierServiceApi supplierServiceApi;

    /**
     * ProductSku -> ProductSkuVO
     */
    public ProductSkuDTO skuVO(ProductSku sku) {
        return this.skuVO(sku,
                productCategoryService.find(sku.getCategoryCode()),
                supplierServiceApi.findByCode(sku.getSupplierCode()),
                unitMapper.findByCode(sku.getUnitCode()));
    }

    private ProductSkuDTO skuVO(ProductSku sku,
                                ProductCategoryVO category,
                                SupplierVO supplier,
                                ProductUnitDO unit) {
        ProductSkuDTO skuVO = new ProductSkuDTO();
        skuVO.setCode(sku.getCode());
        skuVO.setName(sku.getName());
        skuVO.setCategoryCode(category.getCode());
        skuVO.setCategoryName(category.getName());
        skuVO.setCategoryFullName(category.getFullPathName());
        skuVO.setSupplierCode(supplier.getCode());
        skuVO.setSupplierName(supplier.getName());
        skuVO.setUnitCode(unit.getCode());
        skuVO.setUnitName(unit.getName());
        skuVO.setDefaultImage(sku.getDefaultImage());
        skuVO.setImages(sku.getImages());
        skuVO.setDescription(sku.getDescription());
        skuVO.setAttributes(sku.getAttributes());
        skuVO.setAttributesDesc(Attribute.join(sku.getAttributes()));

        return skuVO;
    }

    /**
     * ProductSku -> ProductSkuVO
     */
    public List<ProductSkuDTO> skuVOs(Collection<ProductSku> skus) {
        Set<String> supplierCodes = new HashSet<>();
        Set<String> unitCodes = new HashSet<>();
        for (ProductSku sku : skus) {
            supplierCodes.add(sku.getSupplierCode());
            unitCodes.add(sku.getUnitCode());
        }

        List<ProductCategoryVO> productCategories = productCategoryService.all();
        Map<String, ProductCategoryVO> categoryMap = productCategories.stream()
                .collect(Collectors.toMap(ProductCategoryVO::getCode, e -> e));
        Map<String, SupplierVO> supplierMap = supplierServiceApi.mapByCodes(supplierCodes);
        Map<String, ProductUnitDO> unitMap = unitMapper.mapByCodes(unitCodes);

        return skus.stream()
                .map(sku -> this.skuVO(
                        sku,
                        categoryMap.get(sku.getCategoryCode()),
                        supplierMap.get(sku.getSupplierCode()),
                        unitMap.get(sku.getUnitCode()))
                ).collect(Collectors.toList());
    }

}
