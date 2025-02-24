package com.tyrael.kharazim.basicdata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Tyrael Archangel
 * @since 2024/12/11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DictVO implements Serializable {

    private String code;

    private String desc;

    private Boolean allowModifyItem;

}
