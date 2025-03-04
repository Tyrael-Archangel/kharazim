package com.tyrael.kharazim.basicdata.app.domain.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.mybatis.BaseDO;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/19
 */
@Data
public class Family extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 家庭编号
     */
    private String code;

    /**
     * 家庭名称
     */
    private String name;

    /**
     * 家庭户主编码
     */
    private String leaderCode;

    /**
     * 备注信息
     */
    private String remark;

}
