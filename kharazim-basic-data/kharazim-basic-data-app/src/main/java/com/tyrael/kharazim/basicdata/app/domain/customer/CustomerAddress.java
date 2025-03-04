package com.tyrael.kharazim.basicdata.app.domain.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.mybatis.BaseDO;
import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2024/1/7
 */
@Data
public class CustomerAddress extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会员编号
     */
    private String customerCode;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 省
     */
    private String provinceCode;
    private String provinceName;

    /**
     * 市
     */
    private String cityCode;
    private String cityName;

    /**
     * 区（县）
     */
    private String countyCode;
    private String countyName;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 是否为会员的默认地址
     */
    private Boolean defaultAddress;

}
