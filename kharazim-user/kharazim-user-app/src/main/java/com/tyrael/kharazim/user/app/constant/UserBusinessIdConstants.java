package com.tyrael.kharazim.user.app.constant;

import com.tyrael.kharazim.lib.idgenerator.BusinessIdConstant;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2025/2/18
 */
@Getter
@SuppressWarnings("unused")
public enum UserBusinessIdConstants implements BusinessIdConstant<UserBusinessIdConstants> {

    USER("用户编码", "U"),
    ROLE("角色（岗位）编码", "R");

    private final String prefix;
    private final String desc;
    private final int bit;

    UserBusinessIdConstants(String desc) {
        this(desc, DEFAULT_BIT);
    }

    UserBusinessIdConstants(String desc, int bit) {
        this(desc, bit, null);
    }

    UserBusinessIdConstants(String desc, String prefix) {
        this(desc, DEFAULT_BIT, prefix);
    }

    UserBusinessIdConstants(String desc, int bit, String prefix) {
        this.prefix = prefix;
        this.desc = desc;
        this.bit = bit;
    }

}
