package com.tyrael.kharazim.application.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.system.domain.SystemRequestLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
@Mapper
public interface SystemRequestLogMapper extends BaseMapper<SystemRequestLog> {

    /**
     * 最新若干条系统请求日志
     *
     * @param rows 日志条数
     * @return 系统请求日志
     */
    default List<SystemRequestLog> latestRows(int rows) {
        LambdaQueryWrapper<SystemRequestLog> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByDesc(SystemRequestLog::getId);
        return selectPage(new Page<>(1, rows, false), queryWrapper).getRecords();
    }

}
