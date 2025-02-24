package com.tyrael.kharazim.basicdata.provider;

import com.tyrael.kharazim.basicdata.app.service.FileService;
import com.tyrael.kharazim.basicdata.sdk.service.FileServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.io.IOException;

/**
 * @author Tyrael Archangel
 * @since 2025/2/14
 */
@DubboService
@RequiredArgsConstructor
public class FileServiceProvider implements FileServiceApi {

    private final FileService fileService;

    @Override
    public String getUrl(String fileId) {
        return fileService.getUrl(fileId);
    }

    @Override
    public byte[] readBytes(String fileId) throws IOException {
        return fileService.readBytes(fileId);
    }

}
