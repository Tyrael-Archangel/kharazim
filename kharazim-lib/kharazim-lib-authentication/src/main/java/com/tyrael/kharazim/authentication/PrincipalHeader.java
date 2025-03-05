package com.tyrael.kharazim.authentication;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2025/2/11
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrincipalHeader {

    public static final String TOKEN = "ACCESS-TOKEN";
    public static final String USER_ID = "user-id";
    public static final String USER_CODE = "user-code";
    public static final String USER_NAME = "user-name";
    public static final String USER_NICKNAME = "user-nickname";

}

