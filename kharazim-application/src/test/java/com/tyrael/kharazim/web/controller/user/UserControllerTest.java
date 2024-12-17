package com.tyrael.kharazim.web.controller.user;

import com.tyrael.kharazim.application.user.dto.user.request.ChangePasswordRequest;
import com.tyrael.kharazim.application.user.dto.user.request.ModifyUserRequest;
import com.tyrael.kharazim.application.user.dto.user.request.PageUserRequest;
import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static com.tyrael.kharazim.application.user.enums.UserGenderEnum.MALE;

/**
 * @author Tyrael Archangel
 * @since 2023/12/27
 */
@Slf4j
class UserControllerTest extends BaseControllerTest<UserController> {

    UserControllerTest() {
        super(UserController.class);
    }

    @Test
    void get() {
        super.performWhenCall(mockController.getById(1L));
    }

    @Test
    void page() {
        PageUserRequest pageUserRequest = new PageUserRequest();
        pageUserRequest.setKeywords("admin");
        pageUserRequest.setStatus(EnableStatusEnum.ENABLED);
        super.performWhenCall(mockController.page(pageUserRequest));
    }

    @Test
    void modify() {
        ModifyUserRequest modifyUserRequest = new ModifyUserRequest();
        modifyUserRequest.setId(0L);
        modifyUserRequest.setNickName("超级管理员");
        modifyUserRequest.setEnglishName("admin");
        modifyUserRequest.setGender(MALE);
        modifyUserRequest.setRemark("admin");
        modifyUserRequest.setBirthday(LocalDate.of(2023, Month.AUGUST, 1));
        super.performWhenCall(mockController.modify(null, modifyUserRequest));
    }

    @Test
    void changePassword() {

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setNewPassword("123456789");
        changePasswordRequest.setOldPassword("123456");

        super.performWhenCall(mockController.changePassword(super.mockUser(), changePasswordRequest));
    }

    @Test
    void resetPassword() {
        super.performWhenCall(mockController.resetPassword(super.mockAdmin(), 3L));
    }

}
