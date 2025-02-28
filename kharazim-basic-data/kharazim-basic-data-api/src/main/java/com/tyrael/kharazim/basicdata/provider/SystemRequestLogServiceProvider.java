package com.tyrael.kharazim.basicdata.provider;

import com.tyrael.kharazim.basicdata.app.service.SystemRequestLogService;
import com.tyrael.kharazim.basicdata.model.SystemRequestLogVO;
import com.tyrael.kharazim.basicdata.sdk.service.SystemRequestLogServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Tyrael Archangel
 * @since 2025/2/28
 */
@DubboService
@RequiredArgsConstructor
public class SystemRequestLogServiceProvider implements SystemRequestLogServiceApi {

    private final SystemRequestLogService systemRequestLogService;

    @Override
    public void save(SystemRequestLogVO log) {
        systemRequestLogService.save(log);
    }

}
