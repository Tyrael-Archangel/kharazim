package com.tyrael.kharazim.pharmacy.app.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tyrael.kharazim.base.dto.BaseHasNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2024/7/3
 */
@Getter
@AllArgsConstructor
public enum InboundOrderStatus implements BaseHasNameEnum<InboundOrderStatus> {

    WAIT_INBOUND(1, "待入库"),

    INBOUNDING(2, "入库中"),

    INBOUND_FINISHED(3, "入库完结");

    @EnumValue
    private final Integer value;
    private final String name;

}
