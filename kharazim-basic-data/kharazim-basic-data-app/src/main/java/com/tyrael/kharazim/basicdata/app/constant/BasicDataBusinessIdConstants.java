package com.tyrael.kharazim.basicdata.app.constant;

import com.tyrael.kharazim.lib.idgenerator.BusinessIdConstant;
import lombok.Getter;

/**
 * @author Tyrael Archangel
 * @since 2025/2/18
 */
@Getter
@SuppressWarnings("unused")
public enum BasicDataBusinessIdConstants implements BusinessIdConstant<BasicDataBusinessIdConstants> {

    FILE("文件", 10, "F"),
    FILE_DIR("文件目录", 5, "dir"),
    CLINIC("诊所（机构）", "CL"),

    CUSTOMER("会员编码", 10, "CU"),
    CUSTOMER_FAMILY("会员家庭", "CF"),
    CUSTOMER_RECHARGE_CARD("会员储值单", "CRC"),
    CUSTOMER_WALLET_TRANSACTION("会员交易流水号编码", "TRS"),
    ;

    private final String prefix;
    private final String desc;
    private final int bit;

    BasicDataBusinessIdConstants(String desc) {
        this(desc, DEFAULT_BIT);
    }

    BasicDataBusinessIdConstants(String desc, int bit) {
        this(desc, bit, null);
    }

    BasicDataBusinessIdConstants(String desc, String prefix) {
        this(desc, DEFAULT_BIT, prefix);
    }

    BasicDataBusinessIdConstants(String desc, int bit, String prefix) {
        this.prefix = prefix;
        this.desc = desc;
        this.bit = bit;
    }

}
