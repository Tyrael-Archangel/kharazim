package com.tyrael.kharazim.application.product.service.impl;

import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.product.domain.ProductCategoryDO;
import com.tyrael.kharazim.application.product.mapper.ProductCategoryMapper;
import com.tyrael.kharazim.application.product.service.ProductCategoryService;
import com.tyrael.kharazim.application.product.vo.category.AddProductCategoryRequest;
import com.tyrael.kharazim.application.product.vo.category.ProductCategoryTreeNodeDTO;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.common.dto.TreeNode;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CodeGenerator codeGenerator;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(AddProductCategoryRequest addRequest) {
        Long parentId = addRequest.getParentId();
        String code;
        if (parentId != null) {
            ProductCategoryDO parent = productCategoryMapper.selectById(parentId);
            DomainNotFoundException.assertFound(parent, parentId);
            String parentCode = parent.getCode();
            code = parentCode + codeGenerator.next(parentCode, BusinessCodeConstants.PRODUCT_CATEGORY.getBit());
        } else {
            code = codeGenerator.next(BusinessCodeConstants.PRODUCT_CATEGORY);
        }

        ProductCategoryDO productCategory = new ProductCategoryDO();
        productCategory.setParentId(parentId);
        productCategory.setCode(code);
        productCategory.setName(addRequest.getName());
        productCategory.setRemark(addRequest.getRemark());

        productCategoryMapper.insert(productCategory);

        return code;
    }

    @Override
    public ProductCategoryDO getByCode(String code) {
        return productCategoryMapper.findByCode(code);
    }

}
