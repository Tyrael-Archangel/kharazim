package com.tyrael.kharazim.web.controller.product;

import com.tyrael.kharazim.application.product.service.ProductCategoryService;
import com.tyrael.kharazim.application.product.vo.category.ProductCategoryTreeNodeDTO;
import com.tyrael.kharazim.application.product.vo.sku.AddProductRequest;
import com.tyrael.kharazim.application.product.vo.sku.PageProductSkuRequest;
import com.tyrael.kharazim.application.supplier.service.SupplierService;
import com.tyrael.kharazim.application.supplier.vo.ListSupplierRequest;
import com.tyrael.kharazim.application.supplier.vo.SupplierVO;
import com.tyrael.kharazim.common.exception.ShouldNotHappenException;
import com.tyrael.kharazim.common.util.CollectionUtils;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/3/4
 */
class ProductSkuControllerTest extends BaseControllerTest<ProductSkuController> {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private SupplierService supplierService;

    ProductSkuControllerTest() {
        super(ProductSkuController.class);
    }

    @Test
    void getByCode() {
        String code = "P240306000001";
        super.performWhenCall(mockController.getByCode(code));
    }

    @Test
    void create() {

        List<ProductCategoryTreeNodeDTO> categoryTree = productCategoryService.tree();
        ProductCategoryTreeNodeDTO category = CollectionUtils.random(categoryTree);
        while (category != null && CollectionUtils.isNotEmpty(category.getChildren())) {
            category = CollectionUtils.random(category.getChildren());
        }
        ShouldNotHappenException.assertNull(category);

        List<SupplierVO> suppliers = supplierService.list(new ListSupplierRequest());
        SupplierVO supplier = CollectionUtils.random(suppliers);
        ShouldNotHappenException.assertNull(supplier);

        AddProductRequest addRequest = new AddProductRequest();
        addRequest.setName("测试商品");
        addRequest.setCategoryCode(category.getCode());
        addRequest.setSupplierCode(supplier.getCode());
        addRequest.setDefaultImage("");
        addRequest.setDescription("新增测试商品");
        super.performWhenCall(mockController.create(addRequest));
    }

    @Test
    void page() {
        PageProductSkuRequest pageRequest = new PageProductSkuRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

}