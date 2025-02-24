package com.tyrael.kharazim.basicdata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Tyrael Archangel
 * @since 2025/2/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DictItemVO implements Serializable {

    private String dictCode;
    private String key;
    private String value;
    private Integer sort;

}
