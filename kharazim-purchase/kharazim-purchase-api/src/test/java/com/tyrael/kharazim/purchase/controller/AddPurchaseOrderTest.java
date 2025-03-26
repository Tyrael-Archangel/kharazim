package com.tyrael.kharazim.purchase.controller;

import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.sdk.model.ClinicVO;
import com.tyrael.kharazim.product.sdk.model.ProductSkuVO;
import com.tyrael.kharazim.purchase.DubboReferenceHolder;
import com.tyrael.kharazim.purchase.PurchaseApiApplication;
import com.tyrael.kharazim.purchase.app.domain.SupplierDO;
import com.tyrael.kharazim.purchase.app.mapper.SupplierMapper;
import com.tyrael.kharazim.purchase.app.vo.purchaseorder.CreatePurchaseOrderRequest;
import com.tyrael.kharazim.purchase.app.vo.supplier.ListSupplierRequest;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import com.tyrael.kharazim.test.mock.MockRandomPoetry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2024/8/9
 */
@SpringBootTest(classes = PurchaseApiApplication.class)
public class AddPurchaseOrderTest extends BaseControllerTest<PurchaseOrderController> {

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

    public AddPurchaseOrderTest() {
        super(PurchaseOrderController.class);
    }

    @Test
    void create() {

        List<ClinicVO> clinics = dubboReferenceHolder.clinicServiceApi.listAll();
        List<ProductSkuVO> productSkus = dubboReferenceHolder.productServiceApi.listAll();
        List<SupplierDO> suppliers = supplierMapper.list(new ListSupplierRequest());

        int totalCount = random.nextInt(500) + 100;
        for (int i = 0; i < totalCount; i++) {
            ClinicVO clinic = CollectionUtils.random(clinics);
            SupplierDO supplier = CollectionUtils.random(suppliers);

            CreatePurchaseOrderRequest request = new CreatePurchaseOrderRequest();
            request.setClinicCode(clinic.getCode());
            request.setSupplierCode(supplier.getCode());
            request.setRemark(MockRandomPoetry.random());

            int itemCount = Math.min(random.nextInt(20) + 2, (int) (productSkus.size() * 0.7));
            Set<String> skuCodes = new HashSet<>();
            while (skuCodes.size() < itemCount) {
                ProductSkuVO productSku = CollectionUtils.random(productSkus);
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
            super.performWhenCall(mockController.create(request, dubboReferenceHolder.mockUser()));
        }

    }

}