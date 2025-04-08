package com.tyrael.kharazim.diagnosistreatment.controller;

import com.tyrael.kharazim.authentication.PrincipalHolder;
import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.sdk.model.ClinicVO;
import com.tyrael.kharazim.basicdata.sdk.model.CustomerVO;
import com.tyrael.kharazim.diagnosistreatment.DubboReferenceHolder;
import com.tyrael.kharazim.diagnosistreatment.app.vo.prescription.CreatePrescriptionRequest;
import com.tyrael.kharazim.pharmacy.sdk.model.InventoryVO;
import com.tyrael.kharazim.product.sdk.model.SkuPublishedVO;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import com.tyrael.kharazim.test.mock.MockRandomPoetry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/3/28
 */
@SpringBootTest
public class AddPrescriptionTest extends BaseControllerTest<PrescriptionController> {

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

    public AddPrescriptionTest() {
        super(PrescriptionController.class);
    }

    @Test
    void create() {
        List<CustomerVO> customers = dubboReferenceHolder.customerServiceApi.listAll();
        List<ClinicVO> clinics = dubboReferenceHolder.clinicServiceApi.listAll();

        Map<String, List<SkuPublishedVO>> clinicPublishedMap = dubboReferenceHolder.skuPublishServiceApi
                .pageEffective(new PageCommand().setPageSize(PageCommand.MAX_PAGE_SIZE))
                .getData()
                .stream()
                .collect(Collectors.groupingBy(SkuPublishedVO::getClinicCode));

        int totalCount = random.nextInt(300) + 100;
        for (int i = 0; i < totalCount; i++) {
            CustomerVO customer = CollectionUtils.random(customers);
            ClinicVO clinic = CollectionUtils.random(clinics);
            List<SkuPublishedVO> clinicPublished = clinicPublishedMap.get(clinic.getCode());
            Set<String> publishedSkuCodes = clinicPublished.stream()
                    .map(SkuPublishedVO::getSkuCode)
                    .collect(Collectors.toSet());
            List<InventoryVO> usableInventories = dubboReferenceHolder.inventoryQueryServiceApi
                    .queryInventories(clinic.getCode(), publishedSkuCodes)
                    .stream()
                    .filter(e -> e.getUsableQuantity() > 0)
                    .collect(Collectors.toList());

            int productCount = Math.min(random.nextInt(10) + 2, usableInventories.size());
            Map<String, CreatePrescriptionRequest.Product> randomProductsMap = new LinkedHashMap<>();
            while (randomProductsMap.size() < productCount) {
                InventoryVO inventory = CollectionUtils.random(usableInventories);
                String skuCode = inventory.getSkuCode();
                int quantity = Math.min(
                        random.nextInt(inventory.getUsableQuantity()) + 1,
                        random.nextInt(10) + 5);
                randomProductsMap.put(skuCode, new CreatePrescriptionRequest.Product(skuCode, quantity));
            }

            if (!randomProductsMap.isEmpty()) {
                CreatePrescriptionRequest request = new CreatePrescriptionRequest();
                request.setCustomerCode(customer.getCode());
                request.setClinicCode(clinic.getCode());
                request.setRemark(MockRandomPoetry.random());
                request.setProducts(new ArrayList<>(randomProductsMap.values()));
                PrincipalHolder.setPrincipal(dubboReferenceHolder.mockUser());
                super.performWhenCall(mockController.create(request));
            }
        }
    }

}