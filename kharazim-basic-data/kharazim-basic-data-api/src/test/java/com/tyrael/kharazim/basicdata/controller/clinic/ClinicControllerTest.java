package com.tyrael.kharazim.basicdata.controller.clinic;

import com.tyrael.kharazim.basicdata.DubboReferenceHolder;
import com.tyrael.kharazim.basicdata.app.dto.clinic.ListClinicRequest;
import com.tyrael.kharazim.basicdata.app.dto.clinic.ModifyClinicRequest;
import com.tyrael.kharazim.basicdata.app.dto.clinic.PageClinicRequest;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Tyrael Archangel
 * @since 2023/12/31
 */
@Slf4j
@SpringBootTest
class ClinicControllerTest extends BaseControllerTest<ClinicController> {

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

    ClinicControllerTest() {
        super(ClinicController.class);
    }

    @Test
    void page() {
        PageClinicRequest pageRequest = new PageClinicRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

    @Test
    void list() {
        ListClinicRequest listRequest = new ListClinicRequest();
        super.performWhenCall(mockController.list(listRequest));
    }

    @Test
    void modify() {
        ModifyClinicRequest modifyClinicRequest = new ModifyClinicRequest();
        modifyClinicRequest.setCode("CL000001");
        modifyClinicRequest.setName("泰瑞尔诊所");
        modifyClinicRequest.setEnglishName("Dr.Tyrael Archangel Clinic");
        super.performWhenCall(mockController.modify(modifyClinicRequest, dubboReferenceHolder.userServiceApi.mock()));
    }

}