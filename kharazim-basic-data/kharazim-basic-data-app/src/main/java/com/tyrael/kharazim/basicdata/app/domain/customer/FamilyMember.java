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
public class FamilyMember extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String familyCode;

    private String customerCode;

    /**
     * 与家庭户主关系，字典值配置
     */
    private String relationToLeader;

}
