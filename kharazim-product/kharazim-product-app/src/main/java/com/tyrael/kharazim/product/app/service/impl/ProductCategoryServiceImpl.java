package com.tyrael.kharazim.product.app.service.impl;

import com.tyrael.kharazim.base.dto.TreeNode;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;
import com.tyrael.kharazim.product.app.constant.ProductBusinessIdConstants;
import com.tyrael.kharazim.product.app.domain.ProductCategory;
import com.tyrael.kharazim.product.app.mapper.ProductCategoryMapper;
import com.tyrael.kharazim.product.app.service.ProductCategoryService;
import com.tyrael.kharazim.product.app.vo.category.AddProductCategoryRequest;
import com.tyrael.kharazim.product.app.vo.category.ModifyProductCategoryRequest;
import com.tyrael.kharazim.product.app.vo.category.ProductCategoryTreeNodeDTO;
import com.tyrael.kharazim.product.app.vo.category.ProductCategoryVO;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/2/27
 */
@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryMapper productCategoryMapper;
    private final IdGenerator idGenerator;

    @Override
    public List<ProductCategoryTreeNodeDTO> tree() {
        List<ProductCategory> productCategories = productCategoryMapper.listAll();
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
        List<ProductCategoryTreeNodeDTO> tree = TreeNode.build(productCategoryTreeNodes);
        this.setFullPathName(tree, null);
        return tree;
    }

    private void setFullPathName(Collection<ProductCategoryTreeNodeDTO> tree,
                                 ProductCategoryTreeNodeDTO parent) {
        if (tree == null || tree.isEmpty()) {
            return;
        }
        String parentFullPathName = Optional.ofNullable(parent)
                .map(ProductCategoryTreeNodeDTO::getFullPathName)
                .map(String::trim)
                .orElse("");
        for (ProductCategoryTreeNodeDTO dto : tree) {
            if (parentFullPathName.isEmpty()) {
                dto.setFullPathName(dto.getName());
            } else {
                dto.setFullPathName(parentFullPathName + "/" + dto.getName());
            }
            setFullPathName(dto.getChildren(), dto);
        }
    }

    @Override
    public List<ProductCategoryVO> all() {
        List<ProductCategory> productCategories = productCategoryMapper.listAll();
        Map<Long, ProductCategory> productCategoryMap = productCategories.stream()
                .collect(Collectors.toMap(ProductCategory::getId, e -> e));

        return productCategories.stream()
                .map(pc -> ProductCategoryVO.builder()
                        .code(pc.getCode())
                        .name(pc.getName())
                        .remark(pc.getRemark())
                        .fullPathName(this.getFullPathName(pc, productCategoryMap))
                        .build())
                .collect(Collectors.toList());
    }

    private String getFullPathName(ProductCategory productCategory, Map<Long, ProductCategory> productCategoryMap) {
        Long parentId = productCategory.getParentId();
        StringBuilder fullPathName = new StringBuilder(productCategory.getName());
        while (parentId != null) {
            ProductCategory parent = productCategoryMap.get(parentId);
            if (parent == null) {
                return fullPathName.toString();
            }
            fullPathName.insert(0, parent.getName() + "/");
            parentId = parent.getParentId();
        }
        return fullPathName.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(AddProductCategoryRequest addRequest) {
        Long parentId = addRequest.getParentId();
        String code;
        if (parentId != null) {
            ProductCategory parent = productCategoryMapper.selectById(parentId);
            DomainNotFoundException.assertFound(parent, parentId);
            String parentCode = parent.getCode();
            code = parentCode + idGenerator.next(parentCode, ProductBusinessIdConstants.PRODUCT_CATEGORY.getBit());
        } else {
            code = idGenerator.next(ProductBusinessIdConstants.PRODUCT_CATEGORY);
        }

        ProductCategory productCategory = new ProductCategory();
        productCategory.setParentId(parentId);
        productCategory.setCode(code);
        productCategory.setName(addRequest.getName());
        productCategory.setRemark(addRequest.getRemark());

        productCategoryMapper.insert(productCategory);

        return code;
    }

    @Override
    public ProductCategory getByCode(String code) {
        return productCategoryMapper.findByCode(code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(ModifyProductCategoryRequest modifyRequest, AuthUser currentUser) {
        String code = modifyRequest.getCode();
        ProductCategory productCategory = productCategoryMapper.findByCode(code);
        DomainNotFoundException.assertFound(productCategory, code);

        productCategory.setName(modifyRequest.getName());
        productCategory.setRemark(modifyRequest.getRemark());

        productCategoryMapper.updateById(productCategory);
    }

}
