package com.tyrael.kharazim.application.skupublish.converter;

import com.google.common.collect.Sets;
import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.product.domain.ProductCategory;
import com.tyrael.kharazim.application.product.domain.ProductSku;
import com.tyrael.kharazim.application.product.domain.ProductUnitDO;
import com.tyrael.kharazim.application.product.mapper.ProductCategoryMapper;
import com.tyrael.kharazim.application.product.mapper.ProductSkuMapper;
import com.tyrael.kharazim.application.product.mapper.ProductUnitMapper;
import com.tyrael.kharazim.application.skupublish.domain.SkuPublish;
import com.tyrael.kharazim.application.skupublish.vo.SkuPublishVO;
import com.tyrael.kharazim.application.supplier.domain.SupplierDO;
import com.tyrael.kharazim.application.supplier.mapper.SupplierMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/3/26
 */
@Component
@RequiredArgsConstructor
public class SkuPublishConverter {

    private final ProductSkuMapper productSkuMapper;
    private final ClinicMapper clinicMapper;
    private final ProductCategoryMapper categoryMapper;
    private final SupplierMapper supplierMapper;
    private final ProductUnitMapper unitMapper;

    /**
     * SkuPublishes -> SkuPublishVOs
     */
    public List<SkuPublishVO> skuPublishVOs(Collection<SkuPublish> skuPublishes) {
        if (skuPublishes == null || skuPublishes.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> skuCodes = Sets.newHashSet();
        Set<String> clinicCodes = Sets.newHashSet();
        for (SkuPublish skuPublish : skuPublishes) {
            skuCodes.add(skuPublish.getSkuCode());
            clinicCodes.add(skuPublish.getClinicCode());
        }

        Map<String, Clinic> clinicMap = clinicMapper.mapByCodes(clinicCodes);
        Map<String, ProductSku> productSkuMap = productSkuMapper.mapByCodes(skuCodes);

        Set<String> supplierCodes = Sets.newHashSet();
        Set<String> categoryCodes = Sets.newHashSet();
        Set<String> unitCodes = Sets.newHashSet();
        for (ProductSku sku : productSkuMap.values()) {
            supplierCodes.add(sku.getSupplierCode());
            categoryCodes.add(sku.getCategoryCode());
            unitCodes.add(sku.getUnitCode());
        }

        Map<String, ProductCategory> categoryMap = categoryMapper.mapByCodes(categoryCodes);
        Map<String, SupplierDO> supplierMap = supplierMapper.mapByCodes(supplierCodes);
        Map<String, ProductUnitDO> unitMap = unitMapper.mapByCodes(unitCodes);

        return skuPublishes.stream()
                .map(skuPublish -> {
                    ProductSku productSku = productSkuMap.get(skuPublish.getSkuCode());
                    return this.skuPublishVO(skuPublish,
                            productSku,
                            categoryMap.get(productSku.getCategoryCode()),
                            supplierMap.get(productSku.getSupplierCode()),
                            unitMap.get(productSku.getUnitCode()),
                            clinicMap.get(skuPublish.getClinicCode()));

                })
                .collect(Collectors.toList());
    }

    public SkuPublishVO skuPublishVO(SkuPublish skuPublish,
                                     ProductSku productSku,
                                     ProductCategory category,
                                     SupplierDO supplier,
                                     ProductUnitDO productUnit,
                                     Clinic clinic) {
        SkuPublishVO skuPublishVO = new SkuPublishVO();
        skuPublishVO.setCode(skuPublish.getCode());
        skuPublishVO.setSkuCode(productSku.getCode());
        skuPublishVO.setSkuName(productSku.getName());
        skuPublishVO.setClinicCode(clinic.getCode());
        skuPublishVO.setClinicName(clinic.getName());
        skuPublishVO.setCategoryCode(category.getCode());
        skuPublishVO.setCategoryName(category.getName());
        skuPublishVO.setSupplierCode(supplier.getCode());
        skuPublishVO.setSupplierName(supplier.getName());
        skuPublishVO.setUnitCode(productUnit.getCode());
        skuPublishVO.setUnitName(productUnit.getName());
        skuPublishVO.setPrice(skuPublish.getPrice());
        skuPublishVO.setEffectBegin(skuPublish.getEffectBegin());
        skuPublishVO.setEffectEnd(skuPublish.getEffectEnd());
        skuPublishVO.setDefaultImage(productSku.getDefaultImage());
        return skuPublishVO;
    }

}