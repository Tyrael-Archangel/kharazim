package com.tyrael.kharazim.web.controller.clinic;

import com.tyrael.kharazim.application.clinic.vo.AddClinicRequest;
import com.tyrael.kharazim.application.clinic.vo.ListClinicRequest;
import com.tyrael.kharazim.application.clinic.vo.PageClinicRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2023/12/31
 */
class ClinicControllerTest extends BaseControllerTest<ClinicController> {

    public ClinicControllerTest() {
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
        super.performWhenCall(mockController.list(listRequest));
    }

    @Test
    void add() {
        AddClinicRequest addClinicRequest = new AddClinicRequest();
        addClinicRequest.setName("泰瑞尔诊所");
        addClinicRequest.setEnglishName("Dr.Tyrael Clinic");
        super.performWhenCall(mockController.add(addClinicRequest));
    }

}