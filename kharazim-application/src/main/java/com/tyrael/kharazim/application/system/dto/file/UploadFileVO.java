package com.tyrael.kharazim.application.system.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Tyrael Archangel
 * @since 2024/4/13
 */
@Data
public class UploadFileVO {

    @Schema(description = "文件", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "File can't be null")
    private MultipartFile file;

    @Schema(description = "文件名")
    private String fileName;

}
