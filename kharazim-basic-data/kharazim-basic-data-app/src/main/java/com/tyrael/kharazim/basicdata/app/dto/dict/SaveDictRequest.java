package com.tyrael.kharazim.basicdata.app.dto.dict;

import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
public class SaveDictRequest {

    private String code;

    private String desc;

    private Boolean allowModifyItem;

}
