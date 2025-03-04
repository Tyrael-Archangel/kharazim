package com.tyrael.kharazim.basicdata.app.domain.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.mybatis.BaseDO;
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

}
