package com.tyrael.kharazim.application.product.vo.category;

import com.tyrael.kharazim.common.dto.TreeNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.StringJoiner;

/**
 * @author Tyrael Archangel
 * @since 2024/2/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductCategoryTreeNodeDTO extends TreeNode<ProductCategoryTreeNodeDTO, Long> {

    @Schema(description = "商品分类编码")
    private String code;

    @Schema(description = "商品分类名称")
    private String name;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "全路径名称")
    private String fullPathName;

    @Override
    public String toString() {
        return new StringJoiner(", ", ProductCategoryTreeNodeDTO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("parentId=" + parentId)
                .add("code='" + code + "'")
                .add("name='" + name + "'")
                .add("remark='" + remark + "'")
                .toString();
    }

}
