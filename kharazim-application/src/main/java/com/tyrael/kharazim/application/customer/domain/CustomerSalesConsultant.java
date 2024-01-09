package com.tyrael.kharazim.application.customer.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.application.base.BaseDO;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/9
 */
@Data
public class CustomerSalesConsultant extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会员编码
     */
    private String customerCode;

    /**
     * 专属销售顾问编码
     */
    private String salesConsultantCode;

    /**
     * 删除时间戳，用来表示删除的时间，并且用来做唯一索引
     */
    private Long deletedTimestamp;

}
