package com.tyrael.kharazim.application.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
@TableName("`dict_item`")
public class DictItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String value;

    private String dictCode;

    private Integer sort;

    private Boolean enabled;

    private String remark;

    public boolean enable() {
        return Boolean.TRUE.equals(enabled);
    }

}
