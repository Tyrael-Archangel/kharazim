package com.tyrael.kharazim.web.controller.user;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.user.dto.user.request.AddUserRequest;
import com.tyrael.kharazim.application.user.dto.user.request.ChangePasswordRequest;
import com.tyrael.kharazim.application.user.dto.user.request.ModifyUserRequest;
import com.tyrael.kharazim.application.user.dto.user.request.PageUserRequest;
import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import com.tyrael.kharazim.application.user.enums.UserCertificateTypeEnum;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

/**
 * @author Tyrael Archangel
 * @since 2023/12/27
 */
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
    void add() {

        AddUserRequest addUserRequest = new AddUserRequest();
        addUserRequest.setName("Tyrael");
        addUserRequest.setNickName("泰瑞尔");
        addUserRequest.setGender(UserGenderEnum.MALE);
        addUserRequest.setPhone("13812341234");
        addUserRequest.setCertificateType(UserCertificateTypeEnum.ID_CARD);
        addUserRequest.setCertificateCode("510823202308010001");
        addUserRequest.setRoleId(1L);
        addUserRequest.setBirthday(LocalDate.of(2023, Month.AUGUST, 1));

        super.performWhenCall(mockController.add(addUserRequest));
    }

    @Test
    void modify() {
        ModifyUserRequest modifyUserRequest = new ModifyUserRequest();
        modifyUserRequest.setId(0L);
        modifyUserRequest.setNickName("超级管理员");
        modifyUserRequest.setEnglishName("admin");
        modifyUserRequest.setGender(UserGenderEnum.MALE);
        modifyUserRequest.setRemark("admin");
        modifyUserRequest.setBirthday(LocalDate.of(2023, Month.AUGUST, 1));
        super.performWhenCall(mockController.modify(null, modifyUserRequest));
    }

    @Test
    void changePassword() {

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setNewPassword("123456789");
        changePasswordRequest.setOldPassword("123456");

        AuthUser authUser = new AuthUser();
        authUser.setId(1L);
        authUser.setSuperAdmin(true);
        authUser.setName("admin");
        authUser.setNickName("超级管理员");

        super.performWhenCall(mockController.changePassword(authUser, changePasswordRequest));
    }

    @Test
    void resetPassword() {
        super.performWhenCall(mockController.resetPassword(super.mockAdmin(), 3L));
    }

}
