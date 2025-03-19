package com.tyrael.kharazim.product.app.service.impl;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;
import com.tyrael.kharazim.product.app.constant.ProductBusinessIdConstants;
import com.tyrael.kharazim.product.app.domain.ProductUnitDO;
import com.tyrael.kharazim.product.app.mapper.ProductUnitMapper;
import com.tyrael.kharazim.product.app.service.ProductUnitService;
import com.tyrael.kharazim.product.app.vo.unit.*;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
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
    private final IdGenerator idGenerator;

    @Override
    public PageResponse<ProductUnitVO> page(PageProductUnitRequest pageRequest) {
        PageResponse<ProductUnitDO> pageData = productUnitMapper.page(pageRequest);
        List<ProductUnitVO> units = pageData.getData()
                .stream()
                .map(this::unitVO)
                .collect(Collectors.toList());
        return PageResponse.success(units, pageData.getTotalCount());
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
        unit.setCode(idGenerator.next(ProductBusinessIdConstants.PRODUCT_UNIT));
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
        unit.setUpdateUser(currentUser.getCode(), currentUser.getNickName());

        productUnitMapper.updateById(unit);
    }

}
