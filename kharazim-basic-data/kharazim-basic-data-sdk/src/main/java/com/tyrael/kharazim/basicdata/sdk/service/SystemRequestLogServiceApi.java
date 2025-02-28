package com.tyrael.kharazim.basicdata.sdk.service;

import com.tyrael.kharazim.basicdata.model.SystemRequestLogVO;

/**
 * @author Tyrael Archangel
 * @since 2025/2/28
 */
public interface SystemRequestLogServiceApi {

    /**
     * send log
     *
     * @param log log
     */
    void save(SystemRequestLogVO log);

}
