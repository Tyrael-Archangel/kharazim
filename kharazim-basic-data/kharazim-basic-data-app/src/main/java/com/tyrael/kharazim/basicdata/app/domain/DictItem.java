package com.tyrael.kharazim.basicdata.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("`dict_item`")
public class DictItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String dictCode;

    @TableField("`key`")
    private String key;

    private String value;

    private Integer sort;

}
