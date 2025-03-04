package com.tyrael.kharazim.basicdata.controller.file;

import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.basicdata.app.dto.file.FileVO;
import com.tyrael.kharazim.basicdata.app.dto.file.UploadFileVO;
import com.tyrael.kharazim.basicdata.app.service.file.FileService;
import com.tyrael.kharazim.user.api.sdk.annotation.CurrentUser;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Tyrael Archangel
 * @since 2024/4/11
 */
@RestController
@RequestMapping("/system/file")
@RequiredArgsConstructor
@Tag(name = "文件管理")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传文件，返回文件ID")
    public DataResponse<FileVO> upload(@Parameter(description = "文件") MultipartFile file,
                                       @Parameter(description = "文件名") String fileName,
                                       @Schema(hidden = true) @CurrentUser AuthUser currentUser) throws IOException {
        UploadFileVO fileVO = new UploadFileVO();
        fileVO.setFile(file);
        fileVO.setFileName(fileName);
        return DataResponse.success(fileService.upload(fileVO, currentUser));
    }

    @GetMapping("/fetch/{fileId}")
    @Operation(summary = "下载文件")
    public void download(@PathVariable("fileId") @Parameter(description = "文件ID", required = true) String fileId,
                         HttpServletResponse httpServletResponse) throws IOException {
        fileService.download(fileId, httpServletResponse);
    }

}
