package com.tyrael.kharazim.basicdata.app.mapper.requestlog;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.basicdata.app.domain.requestlog.SystemRequestLog;
import com.tyrael.kharazim.basicdata.app.dto.requestlog.PageSystemRequestLogRequest;
import com.tyrael.kharazim.mybatis.BasePageMapper;
import com.tyrael.kharazim.mybatis.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
                .eqIfHasText(SystemRequestLog::getEndpoint, pageCommand.getEndpoint())
                .eqIfHasText(SystemRequestLog::getUserCode, pageCommand.getUserCode())
                .geIfPresent(SystemRequestLog::getStartTime, pageCommand.getStartTimeRangeBegin())
                .leIfPresent(SystemRequestLog::getStartTime, pageCommand.getStartTimeRangeEnd())
                .geIfPresent(SystemRequestLog::getEndTime, pageCommand.getEndTimeRangeBegin())
                .leIfPresent(SystemRequestLog::getEndTime, pageCommand.getEndTimeRangeEnd());
        queryWrapper.orderByDesc(SystemRequestLog::getEndTime);
        queryWrapper.orderByDesc(SystemRequestLog::getId);
        return page(pageCommand, queryWrapper);
    }

}
