package com.tyrael.kharazim.application.product.service.impl;

import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.product.converter.ProductSpuConverter;
import com.tyrael.kharazim.application.product.domain.ProductSpu;
import com.tyrael.kharazim.application.product.mapper.ProductSpuMapper;
import com.tyrael.kharazim.application.product.service.ProductSpuService;
import com.tyrael.kharazim.application.product.vo.spu.AddProductSpuRequest;
import com.tyrael.kharazim.application.product.vo.spu.PageProductSpuRequest;
import com.tyrael.kharazim.application.product.vo.spu.ProductSpuVO;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
@Service
@RequiredArgsConstructor
public class ProductSpuServiceImpl implements ProductSpuService {

    private static final String SPU_CODE_PREFIX = "SP";

    private final ProductSpuMapper productSpuMapper;
    private final ProductSpuConverter productSpuConverter;
    private final CodeGenerator codeGenerator;

    @Override
    @Transactional(readOnly = true)
    public ProductSpuVO getByCode(String code) {
        ProductSpu spu = productSpuMapper.getByCode(code);
        DomainNotFoundException.assertFound(spu, code);

        return productSpuConverter.spuVO(spu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(AddProductSpuRequest addRequest) {

        ProductSpu spu = new ProductSpu();
        spu.setCode(this.generateSpuCode());
        spu.setName(addRequest.getName());
        spu.setCategoryCode(addRequest.getCategoryCode());
        spu.setSupplierCode(addRequest.getSupplierCode());
        spu.setDefaultImage(addRequest.getDefaultImage());
        spu.setDescription(addRequest.getDescription());

        productSpuMapper.insert(spu);
        return spu.getCode();
    }

    private String generateSpuCode() {
        String timeFormat = "yyMMdd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
        return SPU_CODE_PREFIX
                + formatter.format(LocalDate.now())
                + codeGenerator.next(BusinessCodeConstants.SPU);
    }

    @Override
    public PageResponse<ProductSpuVO> page(PageProductSpuRequest pageRequest) {
        PageResponse<ProductSpu> spuPage = productSpuMapper.page(pageRequest);
        return PageResponse.success(
                productSpuConverter.spuVOs(spuPage.getData()),
                spuPage.getTotalCount(),
                spuPage.getPageSize(),
                spuPage.getPageNum());
    }

}
