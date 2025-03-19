package com.tyrael.kharazim.product.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.product.app.domain.ProductCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/2/27
 */
@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

    /**
     * list all
     *
     * @return all categories
     */
    default List<ProductCategory> listAll() {
        LambdaQueryWrapper<ProductCategory> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByAsc(ProductCategory::getCode);
        return selectList(queryWrapper);
    }

    /**
     * find by code
     *
     * @param code 商品编码
     * @return ProductCategoryDO
     */
    default ProductCategory findByCode(String code) {
        LambdaQueryWrapper<ProductCategory> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ProductCategory::getCode, code);
        return selectOne(queryWrapper);
    }

    /**
     * list by code
     *
     * @param codes codes
     * @return ProductCategories
     */
    default List<ProductCategory> listByCodes(Collection<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<ProductCategory> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(ProductCategory::getCode, codes);
        return selectList(queryWrapper);
    }

    /**
     * map by codes
     *
     * @param codes codes
     * @return code -> ProductCategory
     */
    default Map<String, ProductCategory> mapByCodes(Collection<String> codes) {
        List<ProductCategory> productCategories = this.listByCodes(codes);
        return productCategories.stream()
                .collect(Collectors.toMap(ProductCategory::getCode, e -> e));
    }

    /**
     * 验证分类存在
     *
     * @param code 分类编码
     */
    default void ensureCategoryExist(String code) {
        LambdaQueryWrapper<ProductCategory> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ProductCategory::getCode, code);
        if (!this.exists(queryWrapper)) {
            throw new DomainNotFoundException("categoryCode: " + code);
        }
    }

}
