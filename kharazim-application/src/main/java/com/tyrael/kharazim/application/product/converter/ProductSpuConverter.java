package com.tyrael.kharazim.application.product.converter;

import com.tyrael.kharazim.application.product.domain.ProductCategoryDO;
import com.tyrael.kharazim.application.product.domain.ProductSpu;
import com.tyrael.kharazim.application.product.vo.spu.ProductSpuVO;
import com.tyrael.kharazim.application.supplier.domain.SupplierDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
@Component
@RequiredArgsConstructor
public class ProductSpuConverter {

    /**
     * ProductSpu -> ProductSpuVO
     */
    public ProductSpuVO spuVO(ProductSpu spu,
                              ProductCategoryDO category,
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

}
