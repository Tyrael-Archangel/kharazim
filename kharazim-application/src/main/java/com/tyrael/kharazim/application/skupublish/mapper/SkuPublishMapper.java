package com.tyrael.kharazim.application.skupublish.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.skupublish.domain.SkuPublish;
import com.tyrael.kharazim.application.skupublish.vo.PageSkuPublishRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

/**
 * @author Tyrael Archangel
 * @since 2024/3/16
 */
@Mapper
public interface SkuPublishMapper extends BaseMapper<SkuPublish> {

    /**
     * 商品发布数据分页
     *
     * @param pageRequest {@link PageSkuPublishRequest}
     * @return 商品发布数据分页
     */
    default PageResponse<SkuPublish> page(PageSkuPublishRequest pageRequest) {
        LambdaQueryWrapperX<SkuPublish> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfHasText(SkuPublish::getSkuCode, pageRequest.getSkuCode());
        queryWrapper.eqIfHasText(SkuPublish::getClinicCode, pageRequest.getClinicCode());

        Page<SkuPublish> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<SkuPublish> pageData = selectPage(page, queryWrapper);

        return PageResponse.success(pageData.getRecords(),
                pageData.getTotal(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
    }

    /**
     * sku + 诊所 + 指定的时间范围 是否存在已发布数据
     *
     * @param skuCode     SKU编码
     * @param clinicCode  诊所编码
     * @param effectBegin 生效时间
     * @param effectEnd   失效时间
     * @return 是否存在已发布数据
     */
    default boolean publishExists(String skuCode, String clinicCode, LocalDateTime effectBegin, LocalDateTime effectEnd) {
        LambdaQueryWrapper<SkuPublish> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SkuPublish::getSkuCode, skuCode)
                .eq(SkuPublish::getClinicCode, clinicCode)
                .eq(SkuPublish::getCanceled, Boolean.FALSE)
                .not(q -> q.ge(SkuPublish::getEffectBegin, effectEnd)
                        .or()
                        .le(SkuPublish::getEffectEnd, effectBegin));
        return selectCount(queryWrapper) > 0L;
    }

    /**
     * find by code
     *
     * @param code 商品发布序列号
     * @return SkuPublish
     */
    default SkuPublish findByCode(String code) {
        LambdaQueryWrapper<SkuPublish> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SkuPublish::getCode, code);
        return selectOne(queryWrapper);
    }

}
