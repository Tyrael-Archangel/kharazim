package com.tyrael.kharazim.application.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tyrael.kharazim.application.system.enums.AddressLevelEnum;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@Data
@TableName("`address`")
public class Address {

    private Long id;

    private String code;

    private String name;

    private Long parentId;

    /**
     * 级别，省、市、县（区）
     */
    private AddressLevelEnum level;

}
