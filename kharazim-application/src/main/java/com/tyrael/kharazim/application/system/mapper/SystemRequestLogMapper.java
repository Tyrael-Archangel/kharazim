package com.tyrael.kharazim.application.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.BasePageMapper;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.system.domain.SystemRequestLog;
import com.tyrael.kharazim.application.system.dto.requestlog.PageSystemRequestLogRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
@Mapper
public interface SystemRequestLogMapper extends BasePageMapper<SystemRequestLog> {

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

    /**
     * page logs
     *
     * @param pageCommand {@link PageSystemRequestLogRequest}
     * @return Page of logs
     */
    default PageResponse<SystemRequestLog> page(PageSystemRequestLogRequest pageCommand) {
        LambdaQueryWrapperX<SystemRequestLog> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfPresent(SystemRequestLog::getResponseStatus, pageCommand.getResponseStatus())
                .eqIfPresent(SystemRequestLog::getEndpoint, pageCommand.getEndpoint())
                .geIfPresent(SystemRequestLog::getStartTime, pageCommand.getStartTimeRangeBegin())
                .leIfPresent(SystemRequestLog::getStartTime, pageCommand.getStartTimeRangeEnd())
                .geIfPresent(SystemRequestLog::getEndTime, pageCommand.getEndTimeRangeBegin())
                .leIfPresent(SystemRequestLog::getEndTime, pageCommand.getEndTimeRangeEnd());
        queryWrapper.orderByDesc(SystemRequestLog::getEndTime);
        queryWrapper.orderByDesc(SystemRequestLog::getId);
        return page(pageCommand, queryWrapper);
    }

    /**
     * all endpoints
     *
     * @return all endpoints
     */
    default Set<String> allHistoryEndpoints() {

        String endpointColumnName = new LambdaQueryWrapper<SystemRequestLog>()
                .select(SystemRequestLog::getEndpoint)
                .getSqlSelect();

        QueryWrapper<SystemRequestLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(endpointColumnName)
                .isNotNull(endpointColumnName)
                .groupBy(endpointColumnName);

        List<SystemRequestLog> endpoints = selectList(queryWrapper);
        return endpoints.stream()
                .map(SystemRequestLog::getEndpoint)
                .collect(Collectors.toSet());
    }

}
