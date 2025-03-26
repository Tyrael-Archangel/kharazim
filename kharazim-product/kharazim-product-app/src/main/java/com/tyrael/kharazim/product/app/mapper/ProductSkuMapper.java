package com.tyrael.kharazim.product.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.mybatis.BasePageMapper;
import com.tyrael.kharazim.mybatis.LambdaQueryWrapperX;
import com.tyrael.kharazim.product.app.domain.ProductSku;
import com.tyrael.kharazim.product.app.vo.sku.PageProductSkuRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
@Mapper
public interface ProductSkuMapper extends BasePageMapper<ProductSku> {

    /**
     * filter codes by name
     *
     * @param name SKU name
     * @return skuCodes
     */
    static List<String> filterSkuCodesByName(String name) {
        @SuppressWarnings("deprecation")
        ProductSkuMapper productSkuMapper = SqlHelper.getMapper(
                ProductSku.class, SqlHelper.sqlSession(ProductSku.class));
        return productSkuMapper.filterCodesByName(name);
    }

    /**
     * list all
     *
     * @return all skus
     */
    default List<ProductSku> listAll() {
        return selectList(new LambdaQueryWrapper<>());
    }

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
        queryWrapper.eqIfHasText(ProductSku::getCode, pageRequest.getCode())
                .likeIfPresent(ProductSku::getName, pageRequest.getName())
                .likeIfPresent(ProductSku::getDescription, pageRequest.getDescription());
        if (CollectionUtils.isNotEmpty(pageRequest.getCategoryCodes())) {
            queryWrapper.and(q -> {
                for (String categoryCode : pageRequest.getCategoryCodes()) {
                    q.or().likeRight(ProductSku::getCategoryCode, categoryCode);
                }
            });
        }
        queryWrapper.inIfPresent(ProductSku::getSupplierCode, pageRequest.getSupplierCodes());
        queryWrapper.orderByAsc(ProductSku::getCode);

        return page(pageRequest, queryWrapper);
    }

}
