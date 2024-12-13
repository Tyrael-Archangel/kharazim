package com.tyrael.kharazim.web.controller.user;

import com.tyrael.kharazim.application.user.dto.role.request.SaveRoleRequest;
import com.tyrael.kharazim.application.user.dto.user.request.PageRoleRequest;
import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/1/5
 */
class RoleControllerTest extends BaseControllerTest<RoleController> {

    RoleControllerTest() {
        super(RoleController.class);
    }

    @Test
    void rolePage() {
        PageRoleRequest pageRoleRequest = new PageRoleRequest();
        super.performWhenCall(mockController.rolePage(pageRoleRequest));
    }

    @Test
    void roleDetail() {
        super.performWhenCall(mockController.roleDetail(1L));
    }

    @Test
    void modify() {
        Long id = 1L;
        SaveRoleRequest modifyRoleRequest = new SaveRoleRequest();
        modifyRoleRequest.setName("管理员");
        modifyRoleRequest.setSort(1);
        modifyRoleRequest.setStatus(EnableStatusEnum.ENABLED);
        super.performWhenCall(mockController.modify(id, modifyRoleRequest));
    }

    @Test
    void delete() {
        String ids = "1";
        super.performWhenCall(mockController.delete(ids));
    }

    @Test
    void disable() {
        Long id = 1L;
        super.performWhenCall(mockController.disable(id));
    }

    @Test
    void enable() {
        Long id = 1L;
        super.performWhenCall(mockController.enable(id));
    }

}
