package com.tyrael.kharazim.application.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.common.dto.PageCommand;
import com.tyrael.kharazim.common.dto.PageResponse;

/**
 * @author Tyrael Archangel
 * @since 2024/8/30
 */
public interface BasePageMapper<T> extends BaseMapper<T> {

    /**
     * select page data
     *
     * @param pageCommand  {@link PageCommand}
     * @param queryWrapper {@link Wrapper}
     * @return page data
     */
    default PageResponse<T> selectPage(PageCommand pageCommand, Wrapper<T> queryWrapper) {

        Page<T> pageCondition = new Page<>(pageCommand.getPageNum(), pageCommand.getPageSize());
        Page<T> pageData = selectPage(pageCondition, queryWrapper);

        return PageResponse.success(pageData.getRecords(),
                pageData.getTotal(),
                (int) pageCondition.getSize(),
                (int) pageCondition.getCurrent());

    }

}
