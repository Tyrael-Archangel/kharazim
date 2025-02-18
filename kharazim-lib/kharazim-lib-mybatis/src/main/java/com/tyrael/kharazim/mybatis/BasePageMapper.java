package com.tyrael.kharazim.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.base.dto.PageResponse;

/**
 * @author Tyrael Archangel
 * @since 2024/8/30
 */
public interface BasePageMapper<T> extends BaseMapper<T> {

    /**
     * select page data
     *
     * @param pageCondition pageCondition
     * @param queryWrapper  {@link Wrapper}
     * @return page data
     */
    default PageResponse<T> page(Page<T> pageCondition, Wrapper<T> queryWrapper) {
        Page<T> pageData = selectPage(pageCondition, queryWrapper);
        return PageResponse.success(pageData.getRecords(), (int) pageData.getTotal());
    }

    /**
     * select page data
     *
     * @param pageCommand  {@link PageCommand}
     * @param queryWrapper {@link Wrapper}
     * @return page data
     */
    default PageResponse<T> page(PageCommand pageCommand, Wrapper<T> queryWrapper) {
        return this.page(new Page<>(pageCommand.getPageIndex(), pageCommand.getPageSize()), queryWrapper);
    }

}
