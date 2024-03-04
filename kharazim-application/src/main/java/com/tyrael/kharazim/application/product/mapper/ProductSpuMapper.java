package com.tyrael.kharazim.application.product.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.product.domain.ProductSpu;
import com.tyrael.kharazim.application.product.vo.spu.PageProductSpuRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
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

    /**
     * page
     *
     * @param pageRequest {@link PageProductSpuRequest}
     * @return spu page data
     */
    default PageResponse<ProductSpu> page(PageProductSpuRequest pageRequest) {
        LambdaQueryWrapperX<ProductSpu> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.likeIfPresent(ProductSpu::getName, pageRequest.getName())
                .likeRightIfPresent(ProductSpu::getCategoryCode, pageRequest.getCategoryCode())
                .likeRightIfPresent(ProductSpu::getSupplierCode, pageRequest.getSupplierCode())
                .likeIfPresent(ProductSpu::getDescription, pageRequest.getDescription());
        queryWrapper.orderByAsc(ProductSpu::getCode);

        Page<ProductSpu> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<ProductSpu> pageResponse = selectPage(page, queryWrapper);
        return PageResponse.success(pageResponse.getRecords(),
                pageResponse.getTotal(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
    }

}
