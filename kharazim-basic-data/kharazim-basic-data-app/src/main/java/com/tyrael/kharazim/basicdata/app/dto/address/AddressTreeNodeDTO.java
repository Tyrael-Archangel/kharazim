package com.tyrael.kharazim.basicdata.app.dto.address;

import com.tyrael.kharazim.base.dto.TreeNode;
import com.tyrael.kharazim.basicdata.app.enums.AddressLevelEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AddressTreeNodeDTO extends TreeNode<AddressTreeNodeDTO, Long> {

    private String code;

    private String name;

    private AddressLevelEnum level;

    @Override
    public String toString() {
        return "AddressTreeNodeDTO{" +
                "id=" + id +
                ", code=" + code + '\'' +
                ", name='" + name + '\'' +
                ", level='" + level + '\'' +
                ", parentId=" + parentId +
                '}';
    }

}
