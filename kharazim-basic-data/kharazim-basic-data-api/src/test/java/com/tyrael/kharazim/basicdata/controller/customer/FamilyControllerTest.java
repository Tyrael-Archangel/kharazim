package com.tyrael.kharazim.basicdata.controller.customer;

import com.tyrael.kharazim.basicdata.DubboReferenceHolder;
import com.tyrael.kharazim.basicdata.app.dto.customer.family.*;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Tyrael Archangel
 * @since 2024/1/19
 */
@SpringBootTest
class FamilyControllerTest extends BaseControllerTest<FamilyController> {

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

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
        super.performWhenCall(mockController.setLeader(customerCode, familyCode, dubboReferenceHolder.userServiceApi.mock()));
    }

    @Test
    void modifyFamilyMemberRelation() {
        ModifyFamilyMemberRelationRequest modifyRelationRequest = new ModifyFamilyMemberRelationRequest();
        modifyRelationRequest.setFamilyCode("CF000001");
        modifyRelationRequest.setCustomerCode("CU0000000001");
        modifyRelationRequest.setRelationToLeader("都是魔王");
        super.performWhenCall(mockController.modifyFamilyMemberRelation(modifyRelationRequest, dubboReferenceHolder.userServiceApi.mock()));
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
