package com.tyrael.kharazim.mock;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUserHolder;
import com.tyrael.kharazim.common.util.RandomStringUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2023/12/27
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MockAuth {

    public static void mockCurrentAdmin() {
        AuthUser currentUser = CurrentUserHolder.getCurrentUser();
        if (currentUser == null) {
            CurrentUserHolder.setCurrentUser(mockAdmin(), RandomStringUtil.make(32));
        }
    }

    public static AuthUser mockAdmin() {
        AuthUser authUser = new AuthUser();
        authUser.setId(1L);
        authUser.setCode("000000");
        authUser.setSuperAdmin(true);
        authUser.setName("admin");
        authUser.setNickName("超级管理员");
        return authUser;
    }
}
