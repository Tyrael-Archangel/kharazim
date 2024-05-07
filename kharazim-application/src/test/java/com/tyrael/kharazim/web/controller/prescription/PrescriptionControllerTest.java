package com.tyrael.kharazim.web.controller.prescription;

import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.clinic.vo.ListClinicRequest;
import com.tyrael.kharazim.application.customer.domain.Customer;
import com.tyrael.kharazim.application.customer.mapper.CustomerMapper;
import com.tyrael.kharazim.application.customer.vo.customer.ListCustomerRequest;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

        int totalCount = random.nextInt(300) + 100;
        for (int i = 0; i < totalCount; i++) {
            Customer customer = CollectionUtils.random(customers);
            Clinic clinic = CollectionUtils.random(clinics);
            List<SkuPublish> clinicPublished = clinicPublishedMap.get(clinic.getCode());

            int productCount = random.nextInt(10) + 2;
            Map<String, CreatePrescriptionRequest.Product> randomProductsMap = new LinkedHashMap<>();
            while (randomProductsMap.size() < productCount) {
                int quantity = random.nextInt(10) + 1;
                SkuPublish skuPublish = CollectionUtils.random(clinicPublished);
                randomProductsMap.put(skuPublish.getCode(),
                        new CreatePrescriptionRequest.Product(skuPublish.getSkuCode(), quantity));
            }

            CreatePrescriptionRequest request = new CreatePrescriptionRequest();
            request.setCustomerCode(customer.getCode());
            request.setClinicCode(clinic.getCode());
            request.setRemark(MockRandomPoetry.random());
            request.setProducts(new ArrayList<>(randomProductsMap.values()));
            super.performWhenCall(mockController.create(request));
        }
    }

}