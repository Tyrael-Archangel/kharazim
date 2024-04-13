package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUser;
import com.tyrael.kharazim.application.system.dto.file.UploadFileVO;
import com.tyrael.kharazim.application.system.service.FileService;
import com.tyrael.kharazim.common.dto.DataResponse;
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
    public DataResponse<String> upload(@Parameter(description = "文件") MultipartFile file,
                                       @Parameter(description = "文件名") String fileName,
                                       @Schema(hidden = true) @CurrentUser AuthUser currentUser) throws IOException {
        UploadFileVO fileVO = new UploadFileVO();
        fileVO.setFile(file);
        fileVO.setFileName(fileName);
        return DataResponse.ok(fileService.upload(fileVO, currentUser));
    }

    @GetMapping("/{fileId}")
    @Operation(summary = "下载文件")
    public void download(@PathVariable("fileId") @Parameter(description = "文件ID", required = true) String fileId,
                         HttpServletResponse httpServletResponse) throws IOException {
        fileService.download(fileId, httpServletResponse);
    }

}
