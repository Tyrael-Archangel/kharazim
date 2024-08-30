package com.tyrael.kharazim.web.controller.prescription;

import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.clinic.vo.ListClinicRequest;
import com.tyrael.kharazim.application.customer.domain.Customer;
import com.tyrael.kharazim.application.customer.mapper.CustomerMapper;
import com.tyrael.kharazim.application.customer.vo.customer.ListCustomerRequest;
import com.tyrael.kharazim.application.pharmacy.domain.Inventory;
import com.tyrael.kharazim.application.pharmacy.mapper.InventoryMapper;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.ListInventoryOfClinicRequest;
import com.tyrael.kharazim.application.prescription.vo.CreatePrescriptionRequest;
import com.tyrael.kharazim.application.prescription.vo.PagePrescriptionRequest;
import com.tyrael.kharazim.application.skupublish.domain.SkuPublish;
import com.tyrael.kharazim.application.skupublish.enums.SkuPublishStatus;
import com.tyrael.kharazim.application.skupublish.mapper.SkuPublishMapper;
import com.tyrael.kharazim.application.skupublish.vo.PageSkuPublishRequest;
import com.tyrael.kharazim.common.dto.PageCommand;
import com.tyrael.kharazim.common.util.CollectionUtils;
import com.tyrael.kharazim.mock.MockRandomPoetry;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/3/28
 */
class PrescriptionControllerTest extends BaseControllerTest<PrescriptionController> {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ClinicMapper clinicMapper;
    @Autowired
    private SkuPublishMapper skuPublishMapper;
    @Autowired
    private InventoryMapper inventoryMapper;

    PrescriptionControllerTest() {
        super(PrescriptionController.class);
    }

    @Test
    void page() {
        PagePrescriptionRequest pageRequest = new PagePrescriptionRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

    @Test
    void detail() {
        String code = "RX202403280001";
        super.performWhenCall(mockController.detail(code));
    }

    @Test
    void create() {
        List<Customer> customers = customerMapper.list(new ListCustomerRequest());
        List<Clinic> clinics = clinicMapper.list(new ListClinicRequest());

        PageSkuPublishRequest pageRequest = new PageSkuPublishRequest();
        pageRequest.setPageSize(PageCommand.MAX_PAGE_SIZE);
        pageRequest.setPublishStatus(SkuPublishStatus.IN_EFFECT);

        Map<String, List<SkuPublish>> clinicPublishedMap = skuPublishMapper.page(pageRequest)
                .getData()
                .stream()
                .collect(Collectors.groupingBy(SkuPublish::getClinicCode));

        int totalCount = 1;
        for (int i = 0; i < totalCount; i++) {
            Customer customer = CollectionUtils.random(customers);
            Clinic clinic = CollectionUtils.random(clinics);
            List<SkuPublish> clinicPublished = clinicPublishedMap.get(clinic.getCode());
            Set<String> publishedSkuCodes = clinicPublished.stream()
                    .map(SkuPublish::getSkuCode)
                    .collect(Collectors.toSet());
            List<Inventory> usableInventories = inventoryMapper.listOfClinic(
                            new ListInventoryOfClinicRequest(clinic.getCode(), publishedSkuCodes))
                    .stream()
                    .filter(e -> e.getUsableQuantity() > 0)
                    .collect(Collectors.toList());

            int productCount = Math.min(random.nextInt(10) + 2, usableInventories.size());
            Map<String, CreatePrescriptionRequest.Product> randomProductsMap = new LinkedHashMap<>();
            while (randomProductsMap.size() < productCount) {
                Inventory inventory = CollectionUtils.random(usableInventories);
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
                super.performWhenCall(mockController.create(request));
            }
        }
    }

}