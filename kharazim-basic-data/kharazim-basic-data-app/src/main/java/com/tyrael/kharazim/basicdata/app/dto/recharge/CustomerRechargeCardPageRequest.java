package com.tyrael.kharazim.basicdata.app.dto.recharge;

import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.basicdata.app.constant.BasicDataDictConstants;
import com.tyrael.kharazim.basicdata.app.enums.CustomerRechargeCardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/2/4
 */
@Data
public class CustomerRechargeCardPageRequest extends PageCommand {

    @Schema(description = "储值单号")
    private String code;

    @Schema(description = "会员编码")
    private String customerCode;

    @Schema(description = "成交员工编码")
    private String traderUserCode;

    @Schema(description = "储值卡项类型")
    private List<String> rechargeCardTypes;

    /**
     * {@link BasicDataDictConstants#CUSTOMER_RECHARGE_STATUS}
     */
    @Schema(description = "状态，字典编码: customer_recharge_status" + CustomerRechargeCardStatus.DESC)
    private List<CustomerRechargeCardStatus> statuses;

    @Schema(description = "储值起始日期")
    private LocalDate rechargeStartDate;

    @Schema(description = "储值结束日期")
    private LocalDate rechargeEndDate;

}
