package com.tyrael.kharazim.basicdata.app.domain.clinic;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.basicdata.app.enums.ClinicStatus;
import com.tyrael.kharazim.mybatis.BaseDO;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/30
 */
@Data
public class Clinic extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 诊所（机构）编码
     */
    private String code;

    /**
     * 诊所（机构）名称
     */
    private String name;

    /**
     * 诊所（机构）英文名称
     */
    private String englishName;

    /**
     * 诊所（机构）图片
     */
    private String image;

    /**
     * 状态
     */
    private ClinicStatus status;

}
