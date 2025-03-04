package com.tyrael.kharazim.basicdata.app.dto.address;

import com.tyrael.kharazim.basicdata.app.constant.BasicDataDictConstants;
import com.tyrael.kharazim.basicdata.app.enums.AddressLevelEnum;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@Data
public class AddressDTO {

    private Long id;

    private String code;

    private String name;

    private Long parentId;

    /**
     * {@link BasicDataDictConstants#SYSTEM_ADDRESS_LEVEL}
     */
    private AddressLevelEnum level;

}
