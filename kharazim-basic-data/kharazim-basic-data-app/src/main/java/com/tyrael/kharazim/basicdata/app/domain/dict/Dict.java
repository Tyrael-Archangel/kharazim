package com.tyrael.kharazim.basicdata.app.domain.dict;

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
 * @since 2024/12/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("`dict`")
public class Dict {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String code;

    @TableField("`desc`")
    private String desc;

    private Boolean allowModifyItem;

}
