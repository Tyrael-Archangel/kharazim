package com.tyrael.kharazim.product.app.service.impl;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;
import com.tyrael.kharazim.product.app.constant.ProductBusinessIdConstants;
import com.tyrael.kharazim.product.app.converter.ProductSkuConverter;
import com.tyrael.kharazim.product.app.domain.ProductSku;
import com.tyrael.kharazim.product.app.mapper.ProductCategoryMapper;
import com.tyrael.kharazim.product.app.mapper.ProductSkuMapper;
import com.tyrael.kharazim.product.app.mapper.ProductUnitMapper;
import com.tyrael.kharazim.product.app.service.ProductSkuService;
import com.tyrael.kharazim.product.app.vo.sku.AddProductRequest;
import com.tyrael.kharazim.product.app.vo.sku.PageProductSkuRequest;
import com.tyrael.kharazim.product.app.vo.sku.ProductSkuVO;
import com.tyrael.kharazim.purchase.sdk.service.SupplierServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
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
    private final ProductCategoryMapper productCategoryMapper;
    private final ProductUnitMapper productUnitMapper;
    private final ProductSkuConverter productSkuConverter;
    private final IdGenerator idGenerator;

    @DubboReference
    private SupplierServiceApi supplierServiceApi;

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
        String unitCode = addRequest.getUnitCode();
        productCategoryMapper.ensureCategoryExist(categoryCode);
        supplierServiceApi.ensureSupplierExist(supplierCode);
        productUnitMapper.ensureUnitExist(unitCode);

        ProductSku sku = new ProductSku();
        sku.setCode(this.generateSkuCode());
        sku.setName(addRequest.getName());
        sku.setCategoryCode(categoryCode);
        sku.setSupplierCode(supplierCode);
        sku.setUnitCode(unitCode);
        sku.setDefaultImage(addRequest.getDefaultImage());
        sku.setImages(addRequest.getImages());
        sku.setDescription(addRequest.getDescription());
        sku.setAttributes(addRequest.getAttributes());

        productSkuMapper.insert(sku);
        return sku.getCode();
    }

    private String generateSkuCode() {
        String timeFormat = "yyMMdd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
        return SKU_CODE_PREFIX
                + formatter.format(LocalDate.now())
                + idGenerator.next(ProductBusinessIdConstants.SKU);
    }

    @Override
    public PageResponse<ProductSkuVO> page(PageProductSkuRequest pageRequest) {
        PageResponse<ProductSku> skuPage = productSkuMapper.page(pageRequest);
        return PageResponse.success(
                productSkuConverter.skuVOs(skuPage.getData()),
                skuPage.getTotalCount());
    }

}
