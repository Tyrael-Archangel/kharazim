package com.tyrael.kharazim.application.product.converter;

import com.google.common.collect.Sets;
import com.tyrael.kharazim.application.product.domain.ProductCategory;
import com.tyrael.kharazim.application.product.domain.ProductSku;
import com.tyrael.kharazim.application.product.domain.ProductUnitDO;
import com.tyrael.kharazim.application.product.mapper.ProductCategoryMapper;
import com.tyrael.kharazim.application.product.mapper.ProductUnitMapper;
import com.tyrael.kharazim.application.product.vo.sku.ProductSkuVO;
import com.tyrael.kharazim.application.supplier.domain.SupplierDO;
import com.tyrael.kharazim.application.supplier.mapper.SupplierMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
@Component
@RequiredArgsConstructor
public class ProductSkuConverter {

    private final ProductCategoryMapper categoryMapper;
    private final SupplierMapper supplierMapper;
    private final ProductUnitMapper unitMapper;

    /**
     * ProductSku -> ProductSkuVO
     */
    public ProductSkuVO skuVO(ProductSku sku) {
        return this.skuVO(sku,
                categoryMapper.findByCode(sku.getCategoryCode()),
                supplierMapper.findByCode(sku.getSupplierCode()),
                unitMapper.findByCode(sku.getUnitCode()));
    }

    private ProductSkuVO skuVO(ProductSku sku,
                               ProductCategory category,
                               SupplierDO supplier,
                               ProductUnitDO unit) {
        ProductSkuVO skuVO = new ProductSkuVO();
        skuVO.setCode(sku.getCode());
        skuVO.setName(sku.getName());
        skuVO.setCategoryCode(category.getCode());
        skuVO.setCategoryName(category.getName());
        skuVO.setSupplierCode(supplier.getCode());
        skuVO.setSupplierName(supplier.getName());
        skuVO.setUnitCode(unit.getCode());
        skuVO.setUnitName(unit.getName());
        skuVO.setDefaultImage(sku.getDefaultImage());
        skuVO.setImages(sku.getImages());
        skuVO.setDescription(sku.getDescription());
        skuVO.setAttributes(sku.getAttributes());

        return skuVO;
    }

    /**
     * ProductSku -> ProductSkuVO
     */
    public List<ProductSkuVO> skuVOs(Collection<ProductSku> skus) {
        Set<String> categoryCodes = Sets.newHashSet();
        Set<String> supplierCodes = Sets.newHashSet();
        Set<String> unitCodes = Sets.newHashSet();
        for (ProductSku sku : skus) {
            categoryCodes.add(sku.getCategoryCode());
            supplierCodes.add(sku.getSupplierCode());
            unitCodes.add(sku.getUnitCode());
        }

        Map<String, ProductCategory> categoryMap = categoryMapper.mapByCodes(categoryCodes);
        Map<String, SupplierDO> supplierMap = supplierMapper.mapByCodes(supplierCodes);
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
