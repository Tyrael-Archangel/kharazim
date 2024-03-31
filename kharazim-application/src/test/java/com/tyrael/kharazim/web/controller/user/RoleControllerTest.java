package com.tyrael.kharazim.web.controller.user;

import com.google.common.collect.Lists;
import com.tyrael.kharazim.application.user.dto.role.request.SaveRoleRequest;
import com.tyrael.kharazim.application.user.dto.user.request.PageRoleRequest;
import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    void getRoleMenuIds() {
        Long id = 0L;
        super.performWhenCall(mockController.getRoleMenuIds(id));
    }

    @Test
    void updateRoleMenus() {
        Long id = 1L;
        List<Long> menus = Lists.newArrayList(1L, 2L, 3L);
        super.performWhenCall(mockController.updateRoleMenus(id, menus));
    }

}
