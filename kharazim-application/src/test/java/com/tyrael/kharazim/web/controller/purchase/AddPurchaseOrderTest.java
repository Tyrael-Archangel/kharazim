package com.tyrael.kharazim.web.controller.purchase;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.clinic.vo.ListClinicRequest;
import com.tyrael.kharazim.application.product.domain.ProductSku;
import com.tyrael.kharazim.application.product.mapper.ProductSkuMapper;
import com.tyrael.kharazim.application.purchase.vo.request.CreatePurchaseOrderRequest;
import com.tyrael.kharazim.application.supplier.domain.SupplierDO;
import com.tyrael.kharazim.application.supplier.mapper.SupplierMapper;
import com.tyrael.kharazim.application.supplier.vo.ListSupplierRequest;
import com.tyrael.kharazim.common.util.CollectionUtils;
import com.tyrael.kharazim.mock.MockRandomPoetry;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2024/8/9
 */
public class AddPurchaseOrderTest extends BaseControllerTest<PurchaseOrderController> {

    @Autowired
    private ClinicMapper clinicMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private SupplierMapper supplierMapper;

    public AddPurchaseOrderTest() {
        super(PurchaseOrderController.class);
    }

    @Test
    void create() {

        List<Clinic> clinics = clinicMapper.list(new ListClinicRequest());
        List<ProductSku> productSkus = productSkuMapper.selectList(new LambdaQueryWrapper<>());
        List<SupplierDO> suppliers = supplierMapper.list(new ListSupplierRequest());

        int totalCount = random.nextInt(500) + 100;
        for (int i = 0; i < totalCount; i++) {
            Clinic clinic = CollectionUtils.random(clinics);
            SupplierDO supplier = CollectionUtils.random(suppliers);

            CreatePurchaseOrderRequest request = new CreatePurchaseOrderRequest();
            request.setClinicCode(clinic.getCode());
            request.setSupplierCode(supplier.getCode());
            request.setRemark(MockRandomPoetry.random());

            int itemCount = Math.min(random.nextInt(20) + 2, (int) (productSkus.size() * 0.7));
            Set<String> skuCodes = new HashSet<>();
            while (skuCodes.size() < itemCount) {
                ProductSku productSku = CollectionUtils.random(productSkus);
                skuCodes.add(productSku.getCode());
            }
            List<CreatePurchaseOrderRequest.CreatePurchaseOrderItem> items = new ArrayList<>();
            for (String skuCode : skuCodes) {
                int quantity = random.nextInt(200) + 1;
                int priceFen = (random.nextInt(50) + 2) * 100;
                items.add(CreatePurchaseOrderRequest.CreatePurchaseOrderItem.builder()
                        .skuCode(skuCode)
                        .quantity(quantity)
                        .price(BigDecimal.valueOf(priceFen / 100D))
                        .build());
            }
            request.setItems(items);
            super.performWhenCall(mockController.create(request, super.mockRandomUser()));
        }

    }

}