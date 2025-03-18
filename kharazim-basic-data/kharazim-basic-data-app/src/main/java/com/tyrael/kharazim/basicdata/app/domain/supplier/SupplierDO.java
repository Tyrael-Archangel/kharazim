package com.tyrael.kharazim.basicdata.app.domain.supplier;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tyrael.kharazim.mybatis.BaseDO;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/2/22
 */
@Data
@TableName(value = "supplier", autoResultMap = true)
public class SupplierDO extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 供应商编码
     */
    private String code;

    /**
     * 供应商名称
     */
    private String name;

    /**
     * 备注信息
     */
    private String remark;

}
