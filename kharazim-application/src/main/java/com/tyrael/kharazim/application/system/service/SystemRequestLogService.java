package com.tyrael.kharazim.application.system.service;


import com.tyrael.kharazim.application.system.domain.SystemRequestLog;
import com.tyrael.kharazim.application.system.dto.requestlog.PageSystemRequestLogRequest;
import com.tyrael.kharazim.application.system.dto.requestlog.SystemEndpointDTO;
import com.tyrael.kharazim.application.system.dto.requestlog.SystemRequestLogDTO;
import com.tyrael.kharazim.common.dto.PageResponse;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
public interface SystemRequestLogService {

    /**
     * 保存
     *
     * @param systemRequestLog SystemRequestLog
     */
    void save(SystemRequestLog systemRequestLog);

    /**
     * 最新若干条日志
     *
     * @param rows 条数
     * @return 日志
     */
    List<SystemRequestLogDTO> latestLogs(Integer rows);

    /**
     * 日志分页
     *
     * @param pageCommand {@link PageSystemRequestLogRequest}
     * @return 日志分页数据
     */
    PageResponse<SystemRequestLogDTO> page(PageSystemRequestLogRequest pageCommand);

    /**
     * all endpoints
     *
     * @return all endpoints
     */
    List<SystemEndpointDTO> endpoints();

    /**
     * 禁止endpoint记录日志
     *
     * @param endpoint endpoint
     */
    void disableEndpointLog(String endpoint);

    /**
     * 开启endpoint记录日志
     *
     * @param endpoint endpoint
     */
    void enableEndpointLog(String endpoint);

    /**
     * endpoint是否可以记录日志
     *
     * @param endpoint endpoint
     */
    boolean isEndpointLogEnabled(String endpoint);

}
