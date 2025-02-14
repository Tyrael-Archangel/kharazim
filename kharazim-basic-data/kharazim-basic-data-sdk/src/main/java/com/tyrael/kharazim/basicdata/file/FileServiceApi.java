package com.tyrael.kharazim.basicdata.file;

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

}
