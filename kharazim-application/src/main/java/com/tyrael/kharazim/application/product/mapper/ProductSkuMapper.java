package com.tyrael.kharazim.application.product.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.product.domain.ProductSku;
import com.tyrael.kharazim.application.product.vo.sku.PageProductSkuRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.ibatis.annotations.Mapper;

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
