package com.tyrael.kharazim.basicdata.app.domain.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.mybatis.BaseDO;
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
