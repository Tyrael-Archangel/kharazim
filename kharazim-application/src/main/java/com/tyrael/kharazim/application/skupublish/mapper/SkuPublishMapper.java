package com.tyrael.kharazim.application.skupublish.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
     * 生效的商品发布数据分页
     *
     * @param pageRequest {@link PageSkuPublishRequest}
     * @return 生效的商品发布数据分页
     */
    default PageResponse<SkuPublish> pageEffect(PageSkuPublishRequest pageRequest) {
        // 生效时间 <= now <= 失效时间
        LambdaQueryWrapperX<SkuPublish> queryWrapper = new LambdaQueryWrapperX<>();
        LocalDateTime now = LocalDateTime.now();
        queryWrapper.le(SkuPublish::getEffectBegin, now);
        queryWrapper.ge(SkuPublish::getEffectEnd, now);
        queryWrapper.eqIfHasText(SkuPublish::getSkuCode, pageRequest.getSkuCode());
        queryWrapper.eqIfHasText(SkuPublish::getClinicCode, pageRequest.getClinicCode());

        Page<SkuPublish> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<SkuPublish> pageData = selectPage(page, queryWrapper);

        return PageResponse.success(pageData.getRecords(),
                pageData.getTotal(),
                pageRequest.getPageSize(),
                pageRequest.getPageNum());
    }

}
