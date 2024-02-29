package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.application.system.dto.menu.SaveMenuRequest;
import com.tyrael.kharazim.application.system.enums.MenuTypeEnum;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
class MenuControllerTest extends BaseControllerTest<MenuController> {

    MenuControllerTest() {
        super(MenuController.class);
    }

    @Test
    void add() {
        SaveMenuRequest addMenuRequest = new SaveMenuRequest();
        addMenuRequest.setParentId(-1L);
        addMenuRequest.setName("首页");
        addMenuRequest.setIcon("home");
        addMenuRequest.setPath("/dashboard");
        addMenuRequest.setComponent("/dashboard");
        addMenuRequest.setSort(1);
        addMenuRequest.setVisible(1);
        addMenuRequest.setRedirect("/dashboard");
        addMenuRequest.setType(MenuTypeEnum.MENU);
        addMenuRequest.setPerm(null);
        super.performWhenCall(mockController.add(addMenuRequest));
    }

    @Test
    void modify() {
        Long id = 1L;
        SaveMenuRequest modifyMenuRequest = new SaveMenuRequest();
        modifyMenuRequest.setParentId(-1L);
        modifyMenuRequest.setName("首页");
        modifyMenuRequest.setIcon("home");
        modifyMenuRequest.setPath("/dashboard");
        modifyMenuRequest.setComponent("dashboard");
        modifyMenuRequest.setSort(1);
        modifyMenuRequest.setVisible(1);
        modifyMenuRequest.setRedirect("dashboard");
        modifyMenuRequest.setType(MenuTypeEnum.MENU);
        modifyMenuRequest.setPerm(null);
        super.performWhenCall(mockController.modify(id, modifyMenuRequest));
    }

    @Test
    void delete() {
        Long id = 1L;
        super.performWhenCall(mockController.delete(id));
    }

    @Test
    void disableVisible() {
        Long id = 1L;
        super.performWhenCall(mockController.disableVisible(id));
    }

    @Test
    void enableVisible() {
        Long id = 1L;
        super.performWhenCall(mockController.enableVisible(id));
    }

}
