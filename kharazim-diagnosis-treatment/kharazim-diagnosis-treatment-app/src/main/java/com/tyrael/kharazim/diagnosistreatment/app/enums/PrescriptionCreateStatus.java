package com.tyrael.kharazim.diagnosistreatment.app.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.base.dto.BaseHasNameEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2025/4/7
 */
@Getter
@RequiredArgsConstructor
public enum PrescriptionCreateStatus implements BaseHasNameEnum<PrescriptionCreateStatus> {

    CREATING(1, "创建中"),
    CREATE_SUCCESS(2, "创建成功"),
    CREATE_FAILED(3, "创建失败");

    @EnumValue
    private final Integer value;
    private final String name;

}
