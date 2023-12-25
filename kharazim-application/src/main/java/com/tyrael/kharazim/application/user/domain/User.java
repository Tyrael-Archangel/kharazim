package com.tyrael.kharazim.application.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tyrael.kharazim.application.base.BaseDO;
import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import com.tyrael.kharazim.application.user.enums.UserCertificateTypeEnum;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Data
public class User extends BaseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户编码
     */
    private String code;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户英文名
     */
    private String englishName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private UserGenderEnum gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 手机
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否需要修改密码（新建的用户或者管理员重置密码后需要用户自己修改密码）
     */
    private Boolean needChangePassword;

    /**
     * 用户状态
     */
    private EnableStatusEnum status;

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
     * 证件类型
     */
    private UserCertificateTypeEnum certificateType;

    /**
     * 证件号码
     */
    private String certificateCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除时间戳，用来表示删除的时间，并且用来做唯一索引
     */
    private Long deletedTimestamp;

}
