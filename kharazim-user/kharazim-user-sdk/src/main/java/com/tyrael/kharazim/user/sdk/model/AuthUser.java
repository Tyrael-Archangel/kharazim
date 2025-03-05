package com.tyrael.kharazim.user.sdk.model;

import com.tyrael.kharazim.authentication.Principal;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
@Data
public class AuthUser implements Principal, Serializable {

    private Long id;
    private String code;
    private String name;
    private String nickName;
    private String token;

}
