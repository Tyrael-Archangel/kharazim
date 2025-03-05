package com.tyrael.kharazim.basicdata.app.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2024/4/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {

    @Schema(description = "文件ID")
    private String fileId;

    @Schema(description = "文件链接地址")
    private String url;

}
