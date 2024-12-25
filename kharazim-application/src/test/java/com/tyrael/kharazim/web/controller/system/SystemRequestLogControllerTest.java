package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.application.system.dto.requestlog.PageSystemRequestLogRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

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

    @Test
    void endpoints() {
        super.performWhenCall(mockController.endpoints(new MockHttpServletRequest()));
    }

    @Test
    void disableEndpointLog() {
        super.performWhenCall(mockController.disableEndpointLog("/system/dict/{code}/items  GET"));
    }

    @Test
    void enableEndpointLog() {
        super.performWhenCall(mockController.enableEndpointLog("/system/dict/{code}/items  GET"));
    }

}