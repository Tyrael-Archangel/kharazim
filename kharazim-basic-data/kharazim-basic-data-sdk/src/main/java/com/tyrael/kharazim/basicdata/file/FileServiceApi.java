package com.tyrael.kharazim.basicdata.file;

import java.io.IOException;

/**
 * @author Tyrael Archangel
 * @since 2025/2/14
 */
public interface FileServiceApi {

    /**
     * 获取文件链接地址
     *
     * @param fileId 文件ID
     * @return 文件链接地址
     */
    String getUrl(String fileId);

    /**
     * 获取文件字节数据
     *
     * @param fileId 文件ID
     * @return 文件字节数据
     * @throws IOException IOException
     */
    byte[] readBytes(String fileId) throws IOException;

}
