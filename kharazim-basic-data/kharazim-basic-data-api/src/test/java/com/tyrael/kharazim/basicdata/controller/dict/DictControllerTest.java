package com.tyrael.kharazim.basicdata.controller.dict;

import com.tyrael.kharazim.basicdata.app.constant.BasicDataDictConstants;
import com.tyrael.kharazim.basicdata.app.dto.dict.PageDictRequest;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
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
        super.performWhenCall(mockController.listItems(BasicDataDictConstants.CUSTOMER_GENDER.getCode()));
    }

}
