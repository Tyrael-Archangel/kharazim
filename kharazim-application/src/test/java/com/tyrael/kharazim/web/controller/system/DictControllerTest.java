package com.tyrael.kharazim.web.controller.system;

import com.tyrael.kharazim.application.system.domain.DictConstants;
import com.tyrael.kharazim.application.system.dto.dict.PageDictRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/12/11
 */
class DictControllerTest extends BaseControllerTest<DictController> {

    DictControllerTest() {
        super(DictController.class);
    }

    @Test
    void pageDict() {
        PageDictRequest pageDictRequest = new PageDictRequest();
        super.performWhenCall(mockController.pageDict(pageDictRequest));
    }

    @Test
    void listItems() {
        super.performWhenCall(mockController.listItems(DictConstants.USER_GENDER.getCode()));
    }

}
