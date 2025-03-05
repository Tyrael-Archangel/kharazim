package com.tyrael.kharazim.user;

import com.tyrael.kharazim.basicdata.sdk.service.DictServiceApi;
import com.tyrael.kharazim.basicdata.sdk.service.FileServiceApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2025/3/5
 */
@Component
public class DubboReferenceHolder {

    @DubboReference
    public DictServiceApi dictServiceApi;

    @DubboReference
    public FileServiceApi fileServiceApi;

}
