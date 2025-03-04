package com.tyrael.kharazim.basicdata.app.domain.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.basicdata.app.enums.CustomerCertificateTypeEnum;
import com.tyrael.kharazim.basicdata.app.enums.CustomerGenderEnum;
import com.tyrael.kharazim.mybatis.BaseDO;
import lombok.Data;

import java.time.Year;

/**
 * @author Tyrael Archangel
 * @since 2024/1/7
 */
@Data
public class Customer extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会员编号
     */
    private String code;

    private String name;

    private CustomerGenderEnum gender;

    private Integer birthYear;
    private Integer birthMonth;
    private Integer birthDayOfMonth;

    /**
     * 手机号
     */
    private String phone;
    /**
     * 手机号是否已验证
     */
    private Boolean phoneVerified;

    /**
     * 证件类型
     */
    private CustomerCertificateTypeEnum certificateType;
    /**
     * 证件号码
     */
    private String certificateCode;

    /**
     * 微信号
     */
    private String wechatCode;

    /**
     * 微信unionId
     */
    private String wechatUnionId;

    /**
     * 微信名
     */
    private String wechatName;

    /**
     * 来源渠道字典值
     */
    private String sourceChannelDict;

    /**
     * 推荐（引导）来源会员编码
     */
    private String sourceCustomerCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 年龄，nullable
     */
    public Integer getAge() {
        if (birthYear == null) {
            return null;
        }
        return Year.now().getValue() - birthYear;
    }

}
