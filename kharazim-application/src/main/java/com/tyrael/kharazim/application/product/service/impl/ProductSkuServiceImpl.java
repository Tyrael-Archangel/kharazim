package com.tyrael.kharazim.application.product.service.impl;

import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.product.converter.ProductSkuConverter;
import com.tyrael.kharazim.application.product.domain.ProductSku;
import com.tyrael.kharazim.application.product.mapper.ProductCategoryMapper;
import com.tyrael.kharazim.application.product.mapper.ProductSkuMapper;
import com.tyrael.kharazim.application.product.service.ProductSkuService;
import com.tyrael.kharazim.application.product.vo.sku.AddProductRequest;
import com.tyrael.kharazim.application.product.vo.sku.PageProductSkuRequest;
import com.tyrael.kharazim.application.product.vo.sku.ProductSkuVO;
import com.tyrael.kharazim.application.supplier.mapper.SupplierMapper;
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
public class ProductSkuServiceImpl implements ProductSkuService {

    private static final String SKU_CODE_PREFIX = "P";

    private final ProductSkuMapper productSkuMapper;
    private final ProductSkuConverter productSkuConverter;
    private final ProductCategoryMapper productCategoryMapper;
    private final SupplierMapper supplierMapper;
    private final CodeGenerator codeGenerator;

    @Override
    @Transactional(readOnly = true)
    public ProductSkuVO getByCode(String code) {
        ProductSku sku = productSkuMapper.getByCode(code);
        DomainNotFoundException.assertFound(sku, code);

        return productSkuConverter.skuVO(sku);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(AddProductRequest addRequest) {

        String categoryCode = addRequest.getCategoryCode();
        String supplierCode = addRequest.getSupplierCode();
        productCategoryMapper.ensureCategoryExist(categoryCode);
        supplierMapper.ensureSupplierExist(supplierCode);

        ProductSku sku = new ProductSku();
        sku.setCode(this.generateSkuCode());
        sku.setName(addRequest.getName());
        sku.setCategoryCode(categoryCode);
        sku.setSupplierCode(supplierCode);
        sku.setDefaultImage(addRequest.getDefaultImage());
        sku.setDescription(addRequest.getDescription());

        productSkuMapper.insert(sku);
        return sku.getCode();
    }

    private String generateSkuCode() {
        String timeFormat = "yyMMdd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
        return SKU_CODE_PREFIX
                + formatter.format(LocalDate.now())
                + codeGenerator.next(BusinessCodeConstants.SKU);
    }

    @Override
    public PageResponse<ProductSkuVO> page(PageProductSkuRequest pageRequest) {
        PageResponse<ProductSku> skuPage = productSkuMapper.page(pageRequest);
        return PageResponse.success(
                productSkuConverter.skuVOs(skuPage.getData()),
                skuPage.getTotalCount(),
                skuPage.getPageSize(),
                skuPage.getPageNum());
    }

}
