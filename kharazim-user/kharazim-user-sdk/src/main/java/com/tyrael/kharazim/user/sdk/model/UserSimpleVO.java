package com.tyrael.kharazim.user.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Tyrael Archangel
 * @since 2025/3/4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleVO implements Serializable {

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

}
