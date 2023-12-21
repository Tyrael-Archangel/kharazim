package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
class IndexControllerTest extends BaseControllerTest<IndexController> {

    public IndexControllerTest() {
        super(IndexController.class);
    }

    @Test
    void index() {
        super.performWhenCall(mockController.index());
    }

}