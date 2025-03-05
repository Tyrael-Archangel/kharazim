package com.tyrael.kharazim.basicdata.app.service.file;

import com.tyrael.kharazim.authentication.Principal;
import com.tyrael.kharazim.basicdata.app.dto.file.FileVO;
import com.tyrael.kharazim.basicdata.app.dto.file.UploadFileVO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/4/13
 */
public interface FileService {

    /**
     * 上传文件，返回文件ID
     *
     * @param fileVO      {@link UploadFileVO}
     * @param currentUser 操作人
     * @return 文件ID + 文件URL
     * @throws IOException IOException
     */
    FileVO upload(UploadFileVO fileVO, Principal currentUser) throws IOException;

    /**
     * 下载文件
     *
     * @param fileId              文件ID
     * @param httpServletResponse HttpServletResponse
     * @throws IOException IOException
     */
    void download(String fileId, HttpServletResponse httpServletResponse) throws IOException;

    /**
     * 获取文件字节数据
     *
     * @param fileId 文件ID
     * @return 文件字节数据
     * @throws IOException IOException
     */
    byte[] readBytes(String fileId) throws IOException;

    /**
     * 获取文件链接地址
     *
     * @param fileId 文件ID
     * @return 文件链接地址
     */
    String getUrl(String fileId);

    /**
     * 批量获取文件链接地址
     *
     * @param fileIds 文件IDs
     * @return 文件链接地址
     */
    List<FileVO> getFiles(List<String> fileIds);

}
