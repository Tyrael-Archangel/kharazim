package com.tyrael.kharazim.product.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.mybatis.BasePageMapper;
import com.tyrael.kharazim.mybatis.LambdaQueryWrapperX;
import com.tyrael.kharazim.product.app.domain.SkuPublish;
import com.tyrael.kharazim.product.app.enums.SkuPublishStatus;
import com.tyrael.kharazim.product.app.vo.skupublish.PageSkuPublishRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/3/16
 */
@Mapper
public interface SkuPublishMapper extends BasePageMapper<SkuPublish> {

    /**
     * 商品发布数据分页
     *
     * @param pageRequest {@link PageSkuPublishRequest}
     * @return 商品发布数据分页
     */
    default PageResponse<SkuPublish> page(PageSkuPublishRequest pageRequest) {

        LambdaQueryWrapperX<SkuPublish> queryWrapper = new LambdaQueryWrapperX<>();

        String skuName = pageRequest.getSkuName();
        if (StringUtils.isNotBlank(skuName)) {
            List<String> skuCodes = ProductSkuMapper.filterSkuCodesByName(skuName);
            if (CollectionUtils.isNotEmpty(skuCodes)) {
                queryWrapper.in(SkuPublish::getSkuCode, skuCodes);
            } else {
                return PageResponse.success(new ArrayList<>(), 0);
            }
        }

        queryWrapper.eqIfHasText(SkuPublish::getSkuCode, pageRequest.getSkuCode());
        queryWrapper.inIfPresent(SkuPublish::getClinicCode, pageRequest.getClinicCodes());

        SkuPublishStatus publishStatus = pageRequest.getPublishStatus();
        LocalDateTime now = LocalDateTime.now();
        if (SkuPublishStatus.WAIT_EFFECT.equals(publishStatus)) {
            // 待生效 - 未取消 && 还没到生效开始时间
            queryWrapper.eq(SkuPublish::getCanceled, Boolean.FALSE)
                    .ge(SkuPublish::getEffectBegin, now);
        } else if (SkuPublishStatus.IN_EFFECT.equals(publishStatus)) {
            // 生效中 - 未取消 && 正处于生效时间范围内
            queryWrapper.eq(SkuPublish::getCanceled, Boolean.FALSE)
                    .le(SkuPublish::getEffectBegin, now)
                    .ge(SkuPublish::getEffectEnd, now);
        } else if (SkuPublishStatus.LOST_EFFECT.equals(publishStatus)) {
            // 已失效 - 未取消 && 已经过了生效结束时间
            queryWrapper.eq(SkuPublish::getCanceled, Boolean.FALSE)
                    .le(SkuPublish::getEffectEnd, now);
        } else if (SkuPublishStatus.CANCELED.equals(publishStatus)) {
            // 已取消
            queryWrapper.eq(SkuPublish::getCanceled, Boolean.TRUE);
        }

        queryWrapper.orderByDesc(SkuPublish::getCode);

        return page(pageRequest, queryWrapper);
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

    /**
     * 保存取消发布
     *
     * @param skuPublish SkuPublish
     */
    default void saveCanceled(SkuPublish skuPublish) {
        LambdaUpdateWrapper<SkuPublish> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(SkuPublish::getId, skuPublish.getId());
        updateWrapper.set(SkuPublish::getCanceled, skuPublish.getCanceled());
        updateWrapper.set(SkuPublish::getUpdater, skuPublish.getUpdater());
        updateWrapper.set(SkuPublish::getUpdaterCode, skuPublish.getUpdaterCode());
        updateWrapper.set(SkuPublish::getUpdateTime, skuPublish.getUpdateTime());
        this.update(null, updateWrapper);
    }

    /**
     * 查询诊所有效的商品发布信息
     *
     * @param clinicCode 诊所编码
     * @param skuCodes   SKU编码
     * @return 诊所有效的商品发布信息
     */
    default List<SkuPublish> listClinicEffective(String clinicCode, Collection<String> skuCodes) {
        if (StringUtils.isBlank(clinicCode) || CollectionUtils.isEmpty(skuCodes)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<SkuPublish> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SkuPublish::getCanceled, Boolean.FALSE);
        queryWrapper.eq(SkuPublish::getClinicCode, clinicCode);
        queryWrapper.in(SkuPublish::getSkuCode, skuCodes);
        LocalDateTime now = LocalDateTime.now();
        queryWrapper.le(SkuPublish::getEffectBegin, now);
        queryWrapper.ge(SkuPublish::getEffectEnd, now);

        return selectList(queryWrapper);
    }

}
