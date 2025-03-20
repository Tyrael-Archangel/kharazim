package com.tyrael.kharazim.product.controller;

import com.tyrael.kharazim.authentication.PrincipalHolder;
import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.base.dto.Pair;
import com.tyrael.kharazim.base.exception.ShouldNotHappenException;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.sdk.model.ClinicVO;
import com.tyrael.kharazim.product.DubboReferenceHolder;
import com.tyrael.kharazim.product.ProductApiApplication;
import com.tyrael.kharazim.product.app.service.ProductSkuService;
import com.tyrael.kharazim.product.app.vo.sku.PageProductSkuRequest;
import com.tyrael.kharazim.product.app.vo.sku.ProductSkuVO;
import com.tyrael.kharazim.product.app.vo.skupublish.PublishSkuRequest;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Tyrael Archangel
 * @since 2024/3/26
 */
@SpringBootTest(classes = ProductApiApplication.class)
public class PublishSkuTest extends BaseControllerTest<SkuPublishController> {

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

    public PublishSkuTest() {
        super(SkuPublishController.class);
    }

    @Test
    void randomPublish() {
        PageProductSkuRequest pageSkuRequest = new PageProductSkuRequest();
        pageSkuRequest.setPageSize(PageCommand.MAX_PAGE_SIZE);
        List<String> skuCodes = productSkuService.page(pageSkuRequest)
                .getData()
                .stream()
                .map(ProductSkuVO::getCode)
                .toList();
        List<String> clinicCodes = dubboReferenceHolder.clinicServiceApi.listAll()
                .stream()
                .map(ClinicVO::getCode)
                .toList();

        int totalCount = random.nextInt(500) + 200;
        int canRetryTimes = 20000;
        Set<Pair<String, String>> skuClinicSet = new HashSet<>();
        while (skuClinicSet.size() < totalCount) {
            if (canRetryTimes-- <= 0) {
                throw new ShouldNotHappenException();
            }
            String skuCode = CollectionUtils.random(skuCodes);
            String clinicCode = CollectionUtils.random(clinicCodes);
            skuClinicSet.add(Pair.of(skuCode, clinicCode));
        }

        List<Pair<String, String>> skuClinics = new ArrayList<>(skuClinicSet);
        Collections.shuffle(skuClinics);

        for (Pair<String, String> skuClinic : skuClinics) {
            String skuCode = skuClinic.left();
            String clinic = skuClinic.right();

            BigDecimal price = BigDecimal.valueOf(random.nextDouble(200) + 1)
                    .setScale(2, RoundingMode.HALF_UP);
            LocalDate effectBegin = LocalDate.now().plusDays(random.nextInt(50) - 25);
            LocalDate effectEnd = effectBegin.plusDays(random.nextInt(300) + 1);

            PublishSkuRequest publishRequest = new PublishSkuRequest();
            publishRequest.setSkuCode(skuCode);
            publishRequest.setClinicCode(clinic);
            publishRequest.setPrice(price);
            publishRequest.setEffectBegin(effectBegin.atStartOfDay());
            publishRequest.setEffectEnd(effectEnd.atStartOfDay());

            PrincipalHolder.setPrincipal(dubboReferenceHolder.mockUser());
            super.performWhenCall(mockController.publish(publishRequest));
        }
    }

}