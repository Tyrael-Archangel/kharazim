package com.tyrael.kharazim.user.sdk.model;

import com.tyrael.kharazim.authentication.PrincipalHolder;
import com.tyrael.kharazim.base.util.RandomStringUtil;

/**
 * just use for test
 *
 * @author Tyrael Archangel
 * @since 2025/3/5
 */
public class MockAuthUser {

    public static AuthUser mock() {

        AuthUser mockUser = PrincipalHolder.getPrincipal();
        if (mockUser == null) {
            mockUser = new AuthUser();
            mockUser.setId(0L);
            mockUser.setCode("000000");
            mockUser.setName("admin");
            mockUser.setNickName("超级管理员");
            mockUser.setToken(RandomStringUtil.make(32));
            PrincipalHolder.setPrincipal(mockUser);
        }
        return mockUser;
    }

}
