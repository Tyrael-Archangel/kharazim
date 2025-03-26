package com.tyrael.kharazim.basicdata.sdk.service;

import com.tyrael.kharazim.basicdata.sdk.model.UploadFileVO;

import java.io.IOException;

/**
 * @author Tyrael Archangel
 * @since 2025/2/14
 */
public interface FileServiceApi {

    /**
     * 获取文件字节数据
     *
     * @param fileId 文件ID
     * @return 文件字节数据
     * @throws IOException IOException
     */
    byte[] readBytes(String fileId) throws IOException;

    /**
     * upload file
     *
     * @param file 文件
     * @return 文件ID
     * @throws IOException IOException
     */
    String upload(UploadFileVO file) throws IOException;

}
