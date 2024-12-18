package com.tyrael.kharazim.application.customer.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.application.base.BaseDO;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/18
 */
@Data
public class CustomerTag extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String customerCode;

    private String tagDict;

}
