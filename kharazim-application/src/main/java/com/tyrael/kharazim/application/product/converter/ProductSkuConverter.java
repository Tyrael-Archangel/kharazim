package com.tyrael.kharazim.application.product.converter;

import com.google.common.collect.Sets;
import com.tyrael.kharazim.application.product.domain.ProductSku;
import com.tyrael.kharazim.application.product.domain.ProductUnitDO;
import com.tyrael.kharazim.application.product.mapper.ProductUnitMapper;
import com.tyrael.kharazim.application.product.service.ProductCategoryService;
import com.tyrael.kharazim.application.product.vo.category.ProductCategoryVO;
import com.tyrael.kharazim.application.product.vo.sku.Attribute;
import com.tyrael.kharazim.application.product.vo.sku.ProductSkuVO;
import com.tyrael.kharazim.application.supplier.domain.SupplierDO;
import com.tyrael.kharazim.application.supplier.mapper.SupplierMapper;
import com.tyrael.kharazim.application.system.dto.file.FileUrlVO;
import com.tyrael.kharazim.application.system.service.FileService;
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

    private final ProductCategoryService productCategoryService;
    private final FileService fileService;
    private final SupplierMapper supplierMapper;
    private final ProductUnitMapper unitMapper;

    /**
     * ProductSku -> ProductSkuVO
     */
    public ProductSkuVO skuVO(ProductSku sku) {
        return this.skuVO(sku,
                productCategoryService.find(sku.getCategoryCode()),
                supplierMapper.findByCode(sku.getSupplierCode()),
                unitMapper.findByCode(sku.getUnitCode()));
    }

    private ProductSkuVO skuVO(ProductSku sku,
                               ProductCategoryVO category,
                               SupplierDO supplier,
                               ProductUnitDO unit) {
        ProductSkuVO skuVO = new ProductSkuVO();
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
        skuVO.setDefaultImageUrl(fileService.getUrl(sku.getDefaultImage()));
        skuVO.setImages(sku.getImages());
        List<String> imageUrls = fileService.getUrls(sku.getImages())
                .stream()
                .map(FileUrlVO::getUrl)
                .collect(Collectors.toList());
        skuVO.setImageUrls(imageUrls);
        skuVO.setDescription(sku.getDescription());
        skuVO.setAttributes(sku.getAttributes());
        skuVO.setAttributesDesc(Attribute.join(sku.getAttributes()));

        return skuVO;
    }

    /**
     * ProductSku -> ProductSkuVO
     */
    public List<ProductSkuVO> skuVOs(Collection<ProductSku> skus) {
        Set<String> supplierCodes = Sets.newHashSet();
        Set<String> unitCodes = Sets.newHashSet();
        for (ProductSku sku : skus) {
            supplierCodes.add(sku.getSupplierCode());
            unitCodes.add(sku.getUnitCode());
        }

        List<ProductCategoryVO> productCategories = productCategoryService.all();
        Map<String, ProductCategoryVO> categoryMap = productCategories.stream()
                .collect(Collectors.toMap(ProductCategoryVO::getCode, e -> e));
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
