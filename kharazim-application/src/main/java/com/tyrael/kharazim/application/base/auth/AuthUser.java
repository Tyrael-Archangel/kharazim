package com.tyrael.kharazim.application.base.auth;

import lombok.Data;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
@Data
public class AuthUser {

    private Long id;
    private String code;
    private String name;
    private String nickName;

}
