package com.tyrael.kharazim.application.product.service.impl;

import com.tyrael.kharazim.application.product.converter.ProductSpuConverter;
import com.tyrael.kharazim.application.product.domain.ProductSpu;
import com.tyrael.kharazim.application.product.mapper.ProductCategoryMapper;
import com.tyrael.kharazim.application.product.mapper.ProductSpuMapper;
import com.tyrael.kharazim.application.product.service.ProductSpuService;
import com.tyrael.kharazim.application.product.vo.spu.ProductSpuVO;
import com.tyrael.kharazim.application.supplier.mapper.SupplierMapper;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
@Service
@RequiredArgsConstructor
public class ProductSpuServiceImpl implements ProductSpuService {

    private final ProductSpuMapper productSpuMapper;
    private final SupplierMapper supplierMapper;
    private final ProductCategoryMapper productCategoryMapper;
    private final ProductSpuConverter productSpuConverter;

    @Override
    @Transactional(readOnly = true)
    public ProductSpuVO getByCode(String code) {
        ProductSpu spu = productSpuMapper.getByCode(code);
        DomainNotFoundException.assertFound(spu, code);

        return productSpuConverter.spuVO(
                spu,
                productCategoryMapper.findByCode(spu.getCategoryCode()),
                supplierMapper.findByCode(spu.getSupplierCode()));
    }


}
