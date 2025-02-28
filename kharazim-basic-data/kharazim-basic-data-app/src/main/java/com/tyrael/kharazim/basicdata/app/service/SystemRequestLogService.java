package com.tyrael.kharazim.basicdata.app.service;


import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.basicdata.app.dto.requestlog.PageSystemRequestLogRequest;
import com.tyrael.kharazim.basicdata.app.dto.requestlog.SystemRequestLogDTO;
import com.tyrael.kharazim.basicdata.model.SystemRequestLogVO;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
public interface SystemRequestLogService {

    /**
     * 保存
     *
     * @param logVO SystemRequestLogVO
     */
    void save(SystemRequestLogVO logVO);

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

}
