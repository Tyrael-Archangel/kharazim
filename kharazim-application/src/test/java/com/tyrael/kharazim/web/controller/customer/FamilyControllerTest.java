package com.tyrael.kharazim.web.controller.customer;

import com.tyrael.kharazim.application.customer.vo.family.*;
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
        addFamilyMemberRequest.setCustomerCode("CU0000000001");
        addFamilyMemberRequest.setRelationToLeader("demon");
        super.performWhenCall(mockController.addFamilyMember(addFamilyMemberRequest));
    }

    @Test
    void setLeader() {
        String customerCode = "CU0000000002";
        String familyCode = "CF000001";
        super.performWhenCall(mockController.setLeader(customerCode, familyCode, super.mockAdmin()));
    }

    @Test
    void modifyFamilyMemberRelation() {
        ModifyFamilyMemberRelationRequest modifyRelationRequest = new ModifyFamilyMemberRelationRequest();
        modifyRelationRequest.setFamilyCode("CF000001");
        modifyRelationRequest.setCustomerCode("CU0000000001");
        modifyRelationRequest.setRelationToLeader("都是魔王");
        super.performWhenCall(mockController.modifyFamilyMemberRelation(modifyRelationRequest, super.mockAdmin()));
    }

    @Test
    void leaveFamily() {
        LeaveFamilyRequest leaveFamilyRequest = new LeaveFamilyRequest();
        leaveFamilyRequest.setFamilyCode("CF000001");
        leaveFamilyRequest.setCustomerCode("CU0000000001");
        super.performWhenCall(mockController.leaveFamily(leaveFamilyRequest));
    }

    @Test
    void customerFamily() {
        String customerCode = "CU0000000001";
        super.performWhenCall(mockController.customerFamily(customerCode));
    }

}
