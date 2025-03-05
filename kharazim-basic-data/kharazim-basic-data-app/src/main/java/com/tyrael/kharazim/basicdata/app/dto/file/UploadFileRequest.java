package com.tyrael.kharazim.basicdata.app.dto.file;

import lombok.Data;

import java.io.InputStream;

/**
 * @author Tyrael Archangel
 * @since 2024/4/13
 */
@Data
public class UploadFileRequest {

    private InputStream input;

    private String contentType;

    private String fileName;

}
