package com.tyrael.kharazim.web.controller.clinic;

import com.tyrael.kharazim.application.clinic.vo.ListClinicRequest;
import com.tyrael.kharazim.application.clinic.vo.ModifyClinicRequest;
import com.tyrael.kharazim.application.clinic.vo.PageClinicRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2023/12/31
 */
@Slf4j
class ClinicControllerTest extends BaseControllerTest<ClinicController> {

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
        super.performWhenCall(mockController.modify(modifyClinicRequest, super.mockUser()));
    }

}