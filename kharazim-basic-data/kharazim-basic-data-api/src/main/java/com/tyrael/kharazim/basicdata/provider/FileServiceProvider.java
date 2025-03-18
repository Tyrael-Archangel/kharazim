package com.tyrael.kharazim.basicdata.provider;

import com.tyrael.kharazim.authentication.PrincipalHolder;
import com.tyrael.kharazim.basicdata.app.dto.file.FileDTO;
import com.tyrael.kharazim.basicdata.app.dto.file.UploadFileRequest;
import com.tyrael.kharazim.basicdata.app.service.file.FileService;
import com.tyrael.kharazim.basicdata.sdk.model.FileVO;
import com.tyrael.kharazim.basicdata.sdk.model.UploadFileVO;
import com.tyrael.kharazim.basicdata.sdk.service.FileServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<FileVO> getFiles(List<String> fileIds) {
        List<FileDTO> files = fileService.getFiles(fileIds);
        return files.stream()
                .map(e -> new FileVO(e.getFileId(), e.getUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public byte[] readBytes(String fileId) throws IOException {
        return fileService.readBytes(fileId);
    }

    @Override
    public String upload(UploadFileVO file) throws IOException {
        UploadFileRequest uploadFileRequest = new UploadFileRequest();
        uploadFileRequest.setInput(new ByteArrayInputStream(file.getFileBytes()));
        uploadFileRequest.setFileName(file.getFileName());
        return fileService.upload(uploadFileRequest, PrincipalHolder.getPrincipal()).getFileId();
    }

}
