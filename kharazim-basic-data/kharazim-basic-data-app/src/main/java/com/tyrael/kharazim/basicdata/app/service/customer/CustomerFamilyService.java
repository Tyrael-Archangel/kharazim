package com.tyrael.kharazim.basicdata.app.service.customer;


import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.basicdata.app.dto.customer.family.*;
import com.tyrael.kharazim.user.sdk.model.AuthUser;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/19
 */
public interface CustomerFamilyService {

    /**
     * 查询家庭信息
     *
     * @param familyCode 家庭编码
     * @return 家庭信息
     */
    CustomerFamilyVO family(String familyCode);

    /**
     * 家庭分页
     *
     * @param pageRequest {@link PageFamilyRequest}
     * @return 家庭分页数据
     */
    PageResponse<CustomerFamilyVO> page(PageFamilyRequest pageRequest);

    /**
     * 创建家庭
     *
     * @param createFamilyRequest CreateFamilyRequest
     * @return 家庭编码
     */
    String create(CreateFamilyRequest createFamilyRequest);

    /**
     * 将会员加入家庭
     *
     * @param addFamilyMemberRequest AddFamilyMemberRequest
     */
    void addFamilyMember(AddFamilyMemberRequest addFamilyMemberRequest);

    /**
     * 设置家庭的户主
     *
     * @param customerCode 会员编码
     * @param familyCode   家庭编码
     * @param currentUser  操作人
     */
    void setLeader(String customerCode, String familyCode, AuthUser currentUser);

    /**
     * 修改会员与户主的关系
     *
     * @param modifyFamilyMemberRelationRequest {@link ModifyFamilyMemberRelationRequest}
     * @param currentUser                       操作人
     */
    void modifyFamilyMemberRelation(ModifyFamilyMemberRelationRequest modifyFamilyMemberRelationRequest,
                                    AuthUser currentUser);

    /**
     * 会员退出指定的家庭
     *
     * @param leaveFamilyRequest LeaveFamilyRequest
     */
    void leaveFamily(LeaveFamilyRequest leaveFamilyRequest);

    /**
     * 会员的家庭信息
     *
     * @param customerCode 会员编码
     * @return 会员的家庭信息
     */
    List<CustomerFamilyVO> customerFamily(String customerCode);

}
