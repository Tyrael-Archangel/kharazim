package com.tyrael.kharazim.application.product.service.impl;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.product.domain.ProductUnitDO;
import com.tyrael.kharazim.application.product.mapper.ProductUnitMapper;
import com.tyrael.kharazim.application.product.service.ProductUnitService;
import com.tyrael.kharazim.application.product.vo.unit.*;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/2/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductUnitServiceImpl implements ProductUnitService {

    private final ProductUnitMapper productUnitMapper;
    private final CodeGenerator codeGenerator;

    @Override
    public PageResponse<ProductUnitVO> page(PageProductUnitRequest pageRequest) {
        PageResponse<ProductUnitDO> pageData = productUnitMapper.page(pageRequest);
        List<ProductUnitVO> units = pageData.getData()
                .stream()
                .map(this::unitVO)
                .collect(Collectors.toList());
        return PageResponse.success(units,
                pageData.getTotalCount(),
                pageData.getPageSize(),
                pageData.getPageNum());
    }

    @Override
    public List<ProductUnitVO> list(ListProductUnitRequest listRequest) {
        List<ProductUnitDO> units = productUnitMapper.list(listRequest);
        return units.stream()
                .map(this::unitVO)
                .collect(Collectors.toList());
    }

    private ProductUnitVO unitVO(ProductUnitDO unitDO) {
        return ProductUnitVO.builder()
                .code(unitDO.getCode())
                .name(unitDO.getName())
                .englishName(unitDO.getEnglishName())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(AddProductUnitRequest addUnitRequest) {

        ProductUnitDO unit = new ProductUnitDO();
        unit.setCode(codeGenerator.next(BusinessCodeConstants.PRODUCT_UNIT));
        unit.setName(addUnitRequest.getName());
        unit.setEnglishName(addUnitRequest.getEnglishName());

        try {
            productUnitMapper.insert(unit);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("商品单位已存在", e);
        }

        return unit.getCode();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(ModifyProductUnitRequest modifyUnitRequest, AuthUser currentUser) {
        String unitCode = modifyUnitRequest.getCode();
        ProductUnitDO unit = productUnitMapper.findByCode(unitCode);
        DomainNotFoundException.assertFound(unit, unitCode);

        if (StringUtils.equals(unit.getEnglishName(), modifyUnitRequest.getEnglishName())) {
            // nothing changed
            return;
        }

        unit.setEnglishName(modifyUnitRequest.getEnglishName());
        unit.setUpdate(currentUser.getCode(), currentUser.getNickName());

        productUnitMapper.updateById(unit);
    }

}
