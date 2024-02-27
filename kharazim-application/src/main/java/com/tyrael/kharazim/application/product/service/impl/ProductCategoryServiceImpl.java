package com.tyrael.kharazim.application.product.service.impl;

import com.tyrael.kharazim.application.product.domain.ProductCategoryDO;
import com.tyrael.kharazim.application.product.mapper.ProductCategoryMapper;
import com.tyrael.kharazim.application.product.service.ProductCategoryService;
import com.tyrael.kharazim.application.product.vo.category.ProductCategoryTreeNodeDTO;
import com.tyrael.kharazim.common.dto.TreeNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/2/27
 */
@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryMapper productCategoryMapper;

    @Override
    public List<ProductCategoryTreeNodeDTO> tree() {
        List<ProductCategoryDO> productCategories = productCategoryMapper.listAll();
        List<ProductCategoryTreeNodeDTO> productCategoryTreeNodes = productCategories.stream()
                .map(pc -> {
                    ProductCategoryTreeNodeDTO dto = new ProductCategoryTreeNodeDTO();
                    dto.setId(pc.getId());
                    dto.setParentId(pc.getParentId());
                    dto.setCode(pc.getCode());
                    dto.setName(pc.getName());
                    dto.setRemark(pc.getRemark());
                    return dto;
                })
                .collect(Collectors.toList());
        return TreeNode.build(productCategoryTreeNodes);
    }

}
