package com.tyrael.kharazim.basicdata.provider;

import com.tyrael.kharazim.basicdata.file.FileServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Tyrael Archangel
 * @since 2025/2/14
 */
@DubboService
@RequiredArgsConstructor
public class FileServiceProvider implements FileServiceApi {

    @Override
    public String getUrl(String fileId) {
        return "";
    }

}
