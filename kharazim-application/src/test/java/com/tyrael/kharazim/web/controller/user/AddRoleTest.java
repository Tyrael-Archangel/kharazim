package com.tyrael.kharazim.web.controller.user;

import com.tyrael.kharazim.application.user.dto.role.request.SaveRoleRequest;
import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/9/30
 */
public class AddRoleTest extends BaseControllerTest<RoleController> {

    public AddRoleTest() {
        super(RoleController.class);
    }

    @Test
    void add() {
        List<String> roles = List.of("坦克", "斗士", "近刺", "远刺", "治疗者", "支援者");
        int i = 1;
        for (String role : roles) {
            SaveRoleRequest addRoleRequest = new SaveRoleRequest();
            addRoleRequest.setName(role);
            addRoleRequest.setSort(i++);
            addRoleRequest.setStatus(EnableStatusEnum.ENABLED);
            super.performWhenCall(mockController.add(addRoleRequest));
        }
    }
}
