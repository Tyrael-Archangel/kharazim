package com.tyrael.kharazim.user.controller;

import com.tyrael.kharazim.test.mock.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2025/2/19
 */
public class UserControllerTest extends BaseControllerTest<UserController> {

    public UserControllerTest() {
        super(UserController.class);
    }

    @Test
    void get() {
        super.performWhenCall(mockController.getById(1L));
    }

}
