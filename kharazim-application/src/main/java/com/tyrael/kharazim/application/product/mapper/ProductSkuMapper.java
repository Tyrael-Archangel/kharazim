package com.tyrael.kharazim.application.product.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.product.domain.ProductSku;
import com.tyrael.kharazim.application.product.vo.sku.PageProductSkuRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSku> {

    /**
     * get by code
     *
     * @param code sku编码
     * @return SKU
     */
    default ProductSku getByCode(String code) {
        LambdaQueryWrapper<ProductSku> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ProductSku::getCode, code);
        return selectOne(queryWrapper);
    }

    /**
     * list by codes
     *
     * @param codes sku编码
     * @return SKU
     */
    default List<ProductSku> listByCodes(Collection<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<ProductSku> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(ProductSku::getCode, codes);
        return selectList(queryWrapper);
    }

    /**
     * map by codes
     *
     * @param codes codes
     * @return Map<code, ProductSku>
     */
    default Map<String, ProductSku> mapByCodes(Collection<String> codes) {
        List<ProductSku> productSkus = listByCodes(codes);
        return productSkus.stream()
                .collect(Collectors.toMap(ProductSku::getCode, e -> e));
    }

    /**
     * filter codes by name
     *
     * @param name SKU name
     * @return skuCodes
     */
    default List<String> filterCodesByName(String name) {
        if (StringUtils.isBlank(name)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<ProductSku> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(ProductSku::getName, name);
        queryWrapper.select(ProductSku::getCode);
        List<ProductSku> productSkus = selectList(queryWrapper);
        return productSkus.stream()
                .map(ProductSku::getCode)
                .collect(Collectors.toList());
    }

    /**
     * page
     *
     * @param pageRequest {@link PageProductSkuRequest}
     * @return sku page data
     */
    default PageResponse<ProductSku> page(PageProductSkuRequest pageRequest) {
        LambdaQueryWrapperX<ProductSku> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.likeIfPresent(ProductSku::getName, pageRequest.getName())
                .likeRightIfPresent(ProductSku::getCategoryCode, pageRequest.getCategoryCode())
                .likeRightIfPresent(ProductSku::getSupplierCode, pageRequest.getSupplierCode())
                .likeIfPresent(ProductSku::getDescription, pageRequest.getDescription());
        queryWrapper.orderByAsc(ProductSku::getCode);

        Page<ProductSku> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<ProductSku> pageResponse = selectPage(page, queryWrapper);
        return PageResponse.success(pageResponse.getRecords(),
                pageResponse.getTotal(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
    }

}
