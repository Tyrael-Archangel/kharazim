package com.tyrael.kharazim.application.system.service;


import com.tyrael.kharazim.application.system.domain.SystemRequestLog;
import com.tyrael.kharazim.application.system.dto.requestlog.SystemRequestLogDTO;

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

}
