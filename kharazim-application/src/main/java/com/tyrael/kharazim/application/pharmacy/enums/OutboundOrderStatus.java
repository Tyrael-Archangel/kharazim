package com.tyrael.kharazim.application.pharmacy.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.common.dto.BaseHasNameEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2024/8/15
 */
@Getter
@RequiredArgsConstructor
public enum OutboundOrderStatus implements BaseHasNameEnum<OutboundOrderStatus> {

    WAIT_OUTBOUND(1, "待出库"),
    OUTBOUND_FINISHED(2, "已出库");

    @EnumValue
    private final Integer value;
    private final String name;
}
