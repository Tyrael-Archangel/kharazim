package com.tyrael.kharazim.user.controller;

import com.tyrael.kharazim.authentication.PrincipalHolder;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import com.tyrael.kharazim.user.UserApiApplication;
import com.tyrael.kharazim.user.app.dto.role.request.SaveRoleRequest;
import com.tyrael.kharazim.user.app.enums.EnableStatusEnum;
import com.tyrael.kharazim.user.sdk.service.UserServiceApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/9/30
 */
@SpringBootTest(classes = UserApiApplication.class)
public class AddRoleTest extends BaseControllerTest<RoleController> {

    @Autowired
    private UserServiceApi userServiceApi;

    public AddRoleTest() {
        super(RoleController.class);
    }

    @Test
    void add() {
        List<String> roles = List.of("坦克", "斗士", "近刺", "远刺", "治疗者", "支援者");
        int i = 1;
        for (String role : roles) {
            PrincipalHolder.setPrincipal(userServiceApi.mock());
            SaveRoleRequest addRoleRequest = new SaveRoleRequest();
            addRoleRequest.setName(role);
            addRoleRequest.setSort(i++);
            addRoleRequest.setStatus(EnableStatusEnum.ENABLED);
            super.performWhenCall(mockController.add(addRoleRequest));
        }
    }
}
