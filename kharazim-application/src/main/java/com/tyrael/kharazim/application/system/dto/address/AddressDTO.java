package com.tyrael.kharazim.application.system.dto.address;

import com.tyrael.kharazim.application.system.enums.AddressLevelEnum;
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

    private AddressLevelEnum level;

}
