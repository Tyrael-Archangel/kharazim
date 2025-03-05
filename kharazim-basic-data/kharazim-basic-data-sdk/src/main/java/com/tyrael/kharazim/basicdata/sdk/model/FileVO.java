package com.tyrael.kharazim.basicdata.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Tyrael Archangel
 * @since 2024/4/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileVO implements Serializable {

    private String fileId;

    private String url;

}
