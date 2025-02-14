package com.tyrael.kharazim.user.app.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.lib.base.dto.BaseHasNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Getter
@AllArgsConstructor
public enum UserCertificateTypeEnum implements BaseHasNameEnum<UserCertificateTypeEnum> {

    ID_CARD(1, "身份证"),

    PASSPORT(2, "护照"),

    MILITARY_CARD(3, "军人证");

    public static final String DESC = "ID_CARD-身份证，PASSPORT-护照，MILITARY_CARD-军人证";

    @EnumValue
    private final Integer value;
    private final String name;

}
