package com.tyrael.kharazim.diagnosistreatment.app.service.prescription;

import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.basicdata.sdk.model.ClinicVO;
import com.tyrael.kharazim.basicdata.sdk.service.ClinicServiceApi;
import com.tyrael.kharazim.diagnosistreatment.app.constant.DiagnosisTreatmentBusinessIdConstants;
import com.tyrael.kharazim.diagnosistreatment.app.domain.prescription.Prescription;
import com.tyrael.kharazim.diagnosistreatment.app.domain.prescription.PrescriptionProduct;
import com.tyrael.kharazim.diagnosistreatment.app.enums.PrescriptionCreateStatus;
import com.tyrael.kharazim.diagnosistreatment.app.mapper.PrescriptionMapper;
import com.tyrael.kharazim.diagnosistreatment.app.mapper.PrescriptionProductMapper;
import com.tyrael.kharazim.diagnosistreatment.app.vo.prescription.CreatePrescriptionRequest;
import com.tyrael.kharazim.finance.sdk.model.message.CreatePrescriptionSettlementMessage;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;
import com.tyrael.kharazim.mq.MqProducer;
import com.tyrael.kharazim.pharmacy.sdk.model.InventoryVO;
import com.tyrael.kharazim.pharmacy.sdk.model.message.InventoryOccupyMessage;
import com.tyrael.kharazim.pharmacy.sdk.service.InventoryQueryServiceApi;
import com.tyrael.kharazim.product.sdk.model.ProductSkuVO;
import com.tyrael.kharazim.product.sdk.model.SkuPublishedVO;
import com.tyrael.kharazim.product.sdk.service.ProductServiceApi;
import com.tyrael.kharazim.product.sdk.service.SkuPublishServiceApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/4/8
 */
@Service
@RequiredArgsConstructor
public class PrescriptionLifecycleServiceImpl implements PrescriptionLifecycleService {

    private final PrescriptionMapper prescriptionMapper;
    private final PrescriptionProductMapper prescriptionProductMapper;
    private final IdGenerator idGenerator;
    private final MqProducer mqProducer;

    @DubboReference
    private ClinicServiceApi clinicServiceApi;
    @DubboReference
    private ProductServiceApi productServiceApi;
    @DubboReference
    private SkuPublishServiceApi skuPublishServiceApi;
    @DubboReference
    private InventoryQueryServiceApi inventoryQueryServiceApi;

    /**
     * <pre>
     *     序号         步骤         创建状态
     *      1         校验库存         无
     *      2         创建处方        创建中
     *      3       异步预占库存       创建中
     *      4       处理预占结果
     *     4.1       预占成功         创建完成
     *     4.2       预占失败         创建失败
     * </pre>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(CreatePrescriptionRequest request) {

        this.checkInventory(request);

        Prescription prescription = this.buildPrescription(request);
        this.save(prescription);

        // 异步预占库存
        this.tryOccupyInventory(prescription);

        return prescription.getCode();
    }

    private void checkInventory(CreatePrescriptionRequest request) {
        String clinicCode = request.getClinicCode();
        Set<String> skuCodes = request.getProducts()
                .stream()
                .map(CreatePrescriptionRequest.Product::getSkuCode)
                .collect(Collectors.toSet());
        List<InventoryVO> inventories = inventoryQueryServiceApi.queryInventories(clinicCode, skuCodes);
        Map<String, InventoryVO> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(InventoryVO::getSkuCode, e -> e));

        Set<String> inventoryShortageSkus = new LinkedHashSet<>();
        for (CreatePrescriptionRequest.Product product : request.getProducts()) {
            String skuCode = product.getSkuCode();
            Integer requiredQuantity = product.getQuantity();
            InventoryVO inventory = inventoryMap.get(skuCode);
            if (inventory == null || inventory.getQuantity() < requiredQuantity) {
                inventoryShortageSkus.add(skuCode);
            }
        }
        if (!inventoryShortageSkus.isEmpty()) {
            throw new BusinessException("商品库存不足: " + String.join(", ", inventoryShortageSkus));
        }

    }

    private Prescription buildPrescription(CreatePrescriptionRequest request) {

        ClinicVO clinic = clinicServiceApi.exactlyFindByCode(request.getClinicCode());

        List<CreatePrescriptionRequest.Product> products = request.getProducts();
        Set<String> skuCodes = products.stream()
                .map(CreatePrescriptionRequest.Product::getSkuCode)
                .collect(Collectors.toSet());
        List<ProductSkuVO> productSkus = productServiceApi.listByCodes(skuCodes);
        Map<String, ProductSkuVO> skuMap = productSkus.stream()
                .collect(Collectors.toMap(ProductSkuVO::getCode, e -> e));

        List<SkuPublishedVO> skuPublishes = skuPublishServiceApi.listClinicEffective(clinic.getCode(), skuCodes);
        Map<String, SkuPublishedVO> skuPublishMap = skuPublishes.stream()
                .collect(Collectors.toMap(SkuPublishedVO::getSkuCode, e -> e));

        List<PrescriptionProduct> prescriptionProducts = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CreatePrescriptionRequest.Product product : products) {
            String skuCode = product.getSkuCode();
            ProductSkuVO sku = skuMap.get(skuCode);
            DomainNotFoundException.assertFound(sku, skuCode);

            SkuPublishedVO skuPublish = skuPublishMap.get(skuCode);
            BusinessException.assertTrue(skuPublish != null,
                    "诊所[" + clinic.getName() + "]商品[" + sku.getName() + "]未发布");

            BigDecimal price = skuPublish.getPrice();
            Integer quantity = product.getQuantity();
            BigDecimal amount = price
                    .multiply(BigDecimal.valueOf(quantity));

            PrescriptionProduct prescriptionProduct = new PrescriptionProduct();
            prescriptionProduct.setSkuCode(skuCode);
            prescriptionProduct.setPrice(price);
            prescriptionProduct.setQuantity(quantity);
            prescriptionProduct.setAmount(amount);

            totalAmount = totalAmount.add(amount);

            prescriptionProducts.add(prescriptionProduct);
        }

        Prescription prescription = new Prescription();
        prescription.setCode(idGenerator.dailyNext(DiagnosisTreatmentBusinessIdConstants.PRESCRIPTION));
        prescription.setCustomerCode(request.getCustomerCode());
        prescription.setClinicCode(clinic.getCode());
        prescription.setTotalAmount(totalAmount);
        prescription.setProducts(prescriptionProducts);
        prescription.setRemark(request.getRemark());
        prescription.setCreateStatus(PrescriptionCreateStatus.CREATING);
        prescription.setPaidTime(null);

        return prescription;
    }

    private void save(Prescription prescription) {
        prescriptionMapper.insert(prescription);
        for (PrescriptionProduct product : prescription.getProducts()) {
            product.setPrescriptionCode(prescription.getCode());
        }
        prescriptionProductMapper.saveBatch(prescription.getProducts());
    }

    private void tryOccupyInventory(Prescription prescription) {
        List<InventoryOccupyMessage.Item> items = prescription.getProducts()
                .stream()
                .map(e -> new InventoryOccupyMessage.Item()
                        .setSkuCode(e.getSkuCode())
                        .setQuantity(e.getQuantity()))
                .collect(Collectors.toList());
        InventoryOccupyMessage inventoryOccupyMessage = new InventoryOccupyMessage()
                .setBusinessCode(prescription.getCode())
                .setClinicCode(prescription.getClinicCode())
                .setItems(items)
                .setOperator(prescription.getCreator())
                .setOperatorCode(prescription.getCreatorCode());
        mqProducer.send("INVENTORY_OCCUPY", inventoryOccupyMessage);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void occupyCallback(String code, boolean occupied) {
        Prescription prescription = prescriptionMapper.findByCodeForUpdate(code);
        if (prescription == null || !prescription.isCreating()) {
            return;
        }

        if (occupied) {
            prescription.markCreateSuccess();
            prescriptionMapper.updateById(prescription);

            // 预占成功，创建结算单
            this.pushSettlement(prescription);

        } else {
            prescription.markCreateFailed();
            prescriptionMapper.updateById(prescription);
        }

    }

    private void pushSettlement(Prescription prescription) {
        List<CreatePrescriptionSettlementMessage.Item> items = prescription.getProducts()
                .stream()
                .map(e -> new CreatePrescriptionSettlementMessage.Item()
                        .setSkuCode(e.getSkuCode())
                        .setQuantity(e.getQuantity())
                        .setPrice(e.getPrice())
                        .setAmount(e.getAmount()))
                .collect(Collectors.toList());
        CreatePrescriptionSettlementMessage message = new CreatePrescriptionSettlementMessage()
                .setPrescriptionCode(prescription.getCode())
                .setCustomerCode(prescription.getCustomerCode())
                .setClinicCode(prescription.getClinicCode())
                .setTotalAmount(prescription.getTotalAmount())
                .setItems(items);

        mqProducer.send("CREATE_PRESCRIPTION_SETTLEMENT", message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paidCallback(String code, LocalDateTime paidTime) {
        Prescription prescription = prescriptionMapper.findByCodeForUpdate(code);
        if (prescription.paid()) {
            return;
        }

        prescription.markPaid(paidTime);
        prescriptionMapper.updateById(prescription);

        prescription.setProducts(prescriptionProductMapper.listByPrescriptionCode(code));

        this.pushOutbound(prescription);
    }

    private void pushOutbound(Prescription prescription) {
        // TODO
    }

}
