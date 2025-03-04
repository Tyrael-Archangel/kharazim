package com.tyrael.kharazim.basicdata.app.dto.customer.family;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/19
 */
@Data
public class CustomerFamilyVO {

    @Schema(description = "家庭编码")
    private String familyCode;

    @Schema(description = "家庭名称")
    private String familyName;

    @Schema(description = "家庭户主编码")
    private String leaderCustomerCode;

    @Schema(description = "家庭户主名")
    private String leaderCustomerName;

    @Schema(description = "备注信息")
    private String remark;

    @Schema(description = "家庭成员")
    private List<FamilyMemberVO> familyMembers;

    @Data
    public static class FamilyMemberVO {

        @Schema(description = "家庭成员姓名")
        private String name;

        @Schema(description = "会员编码")
        private String customerCode;

        @Schema(description = "电话")
        private String phone;

        @Schema(description = "与户主的关系")
        private String relationToLeader;

    }

}
