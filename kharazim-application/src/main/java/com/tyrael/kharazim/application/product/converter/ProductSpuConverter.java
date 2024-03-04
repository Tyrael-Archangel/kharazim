package com.tyrael.kharazim.application.product.converter;

import com.google.common.collect.Sets;
import com.tyrael.kharazim.application.product.domain.ProductCategory;
import com.tyrael.kharazim.application.product.domain.ProductSpu;
import com.tyrael.kharazim.application.product.mapper.ProductCategoryMapper;
import com.tyrael.kharazim.application.product.vo.spu.ProductSpuVO;
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
public class ProductSpuConverter {

    private final ProductCategoryMapper categoryMapper;
    private final SupplierMapper supplierMapper;

    /**
     * ProductSpu -> ProductSpuVO
     */
    public ProductSpuVO spuVO(ProductSpu spu) {
        return this.spuVO(spu,
                categoryMapper.findByCode(spu.getCategoryCode()),
                supplierMapper.findByCode(spu.getSupplierCode()));
    }

    private ProductSpuVO spuVO(ProductSpu spu,
                               ProductCategory category,
                               SupplierDO supplier) {
        ProductSpuVO spuVO = new ProductSpuVO();
        spuVO.setCode(spu.getCode());
        spuVO.setName(spu.getName());
        spuVO.setCategoryCode(category.getCode());
        spuVO.setCategoryName(category.getName());
        spuVO.setSupplierCode(supplier.getCode());
        spuVO.setSupplierName(supplier.getName());
        spuVO.setDefaultImage(spu.getDefaultImage());
        spuVO.setDescription(spu.getDescription());

        return spuVO;
    }

    /**
     * ProductSpu -> ProductSpuVO
     */
    public List<ProductSpuVO> spuVOs(Collection<ProductSpu> spus) {
        Set<String> categoryCodes = Sets.newHashSet();
        Set<String> supplierCodes = Sets.newHashSet();
        for (ProductSpu spu : spus) {
            categoryCodes.add(spu.getCategoryCode());
            supplierCodes.add(spu.getSupplierCode());
        }

        Map<String, ProductCategory> categoryMap = categoryMapper.mapByCodes(categoryCodes);
        Map<String, SupplierDO> supplierMap = supplierMapper.mapByCodes(supplierCodes);

        return spus.stream()
                .map(spu -> this.spuVO(
                        spu,
                        categoryMap.get(spu.getCategoryCode()),
                        supplierMap.get(spu.getSupplierCode()))
                ).collect(Collectors.toList());
    }

}
