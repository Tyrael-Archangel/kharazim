package com.tyrael.kharazim.application.product.service.impl;

import com.tyrael.kharazim.application.product.domain.ProductUnitDO;
import com.tyrael.kharazim.application.product.mapper.ProductUnitMapper;
import com.tyrael.kharazim.application.product.service.ProductUnitService;
import com.tyrael.kharazim.application.product.vo.ListProductUnitRequest;
import com.tyrael.kharazim.application.product.vo.PageProductUnitRequest;
import com.tyrael.kharazim.application.product.vo.ProductUnitVO;
import com.tyrael.kharazim.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

}
