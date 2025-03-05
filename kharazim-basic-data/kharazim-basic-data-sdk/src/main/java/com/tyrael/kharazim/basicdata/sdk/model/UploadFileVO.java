package com.tyrael.kharazim.basicdata.sdk.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Tyrael Archangel
 * @since 2024/4/13
 */
@Data
public class UploadFileVO implements Serializable {

    private byte[] fileBytes;

    private String fileName;

}
