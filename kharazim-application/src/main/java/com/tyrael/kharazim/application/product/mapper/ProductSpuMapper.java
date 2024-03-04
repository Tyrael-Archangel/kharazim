package com.tyrael.kharazim.application.product.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.product.domain.ProductSpu;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
@Mapper
public interface ProductSpuMapper extends BaseMapper<ProductSpu> {

    /**
     * get by code
     *
     * @param code spu编码
     * @return SPU
     */
    default ProductSpu getByCode(String code) {
        LambdaQueryWrapper<ProductSpu> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ProductSpu::getCode, code);
        return selectOne(queryWrapper);
    }

}
