package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
class SystemRequestLogControllerTest extends BaseControllerTest<SystemRequestLogController> {

    SystemRequestLogControllerTest() {
        super(SystemRequestLogController.class);
    }

    @Test
    void latestLogs() {
        super.performWhenCall(mockController.latestLogs(20));
    }

}