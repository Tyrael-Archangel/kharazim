package com.tyrael.kharazim.application.customer.vo.family;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tyrael Archangel
 * @since 2024/1/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyFamilyMemberRelationRequest {

    @Schema(description = "家庭编码")
    @NotBlank(message = "家庭编码不能为空")
    private String familyCode;

    @Schema(description = "会员编码")
    @NotBlank(message = "会员编码不能为空")
    private String customerCode;

    @Schema(description = "与家庭户主的关系")
    @NotBlank(message = "与家庭户主的关系不能为空")
    private String relationToLeader;

}
