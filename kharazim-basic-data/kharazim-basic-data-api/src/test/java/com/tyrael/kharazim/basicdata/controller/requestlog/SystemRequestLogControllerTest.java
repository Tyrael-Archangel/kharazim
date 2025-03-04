package com.tyrael.kharazim.basicdata.controller.requestlog;

import com.tyrael.kharazim.basicdata.app.dto.requestlog.PageSystemRequestLogRequest;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
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

    @Test
    void page() {
        PageSystemRequestLogRequest pageCommand = new PageSystemRequestLogRequest();
        super.performWhenCall(mockController.page(pageCommand));
    }

}