package com.tyrael.kharazim.web.controller.customer;

import com.tyrael.kharazim.application.customer.vo.family.AddFamilyMemberRequest;
import com.tyrael.kharazim.application.customer.vo.family.CreateFamilyRequest;
import com.tyrael.kharazim.application.customer.vo.family.PageFamilyRequest;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2024/1/19
 */
class FamilyControllerTest extends BaseControllerTest<FamilyController> {

    FamilyControllerTest() {
        super(FamilyController.class);
    }

    @Test
    void family() {
        String familyCode = "CF000001";
        super.performWhenCall(mockController.family(familyCode));
    }

    @Test
    void page() {
        PageFamilyRequest pageRequest = new PageFamilyRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

    @Test
    void create() {
        CreateFamilyRequest createFamilyRequest = new CreateFamilyRequest();
        createFamilyRequest.setFamilyName("永恒战场");
        createFamilyRequest.setLeaderCustomerCode("CU0000000001");
        createFamilyRequest.setRemark("地域恶魔VS天使");
        super.performWhenCall(mockController.create(createFamilyRequest));
    }

    @Test
    void addFamilyMember() {
        AddFamilyMemberRequest addFamilyMemberRequest = new AddFamilyMemberRequest();
        addFamilyMemberRequest.setFamilyCode("CF000001");
        addFamilyMemberRequest.setCustomerCode("CU0000000002");
        addFamilyMemberRequest.setRelationToLeader("demon");
        super.performWhenCall(mockController.addFamilyMember(addFamilyMemberRequest));
    }

}
