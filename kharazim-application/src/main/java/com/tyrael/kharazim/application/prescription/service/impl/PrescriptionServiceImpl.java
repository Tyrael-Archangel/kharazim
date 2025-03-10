package com.tyrael.kharazim.application.prescription.service.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.clinic.domain.Clinic;
import com.tyrael.kharazim.application.clinic.mapper.ClinicMapper;
import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.customer.mapper.CustomerMapper;
import com.tyrael.kharazim.application.pharmacy.event.CreateOutboundOrderEvent;
import com.tyrael.kharazim.application.pharmacy.service.InventoryService;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.InventoryOccupyCommand;
import com.tyrael.kharazim.application.prescription.converter.PrescriptionConverter;
import com.tyrael.kharazim.application.prescription.domain.Prescription;
import com.tyrael.kharazim.application.prescription.domain.PrescriptionProduct;
import com.tyrael.kharazim.application.prescription.mapper.PrescriptionMapper;
import com.tyrael.kharazim.application.prescription.mapper.PrescriptionProductMapper;
import com.tyrael.kharazim.application.prescription.service.PrescriptionService;
import com.tyrael.kharazim.application.prescription.vo.CreatePrescriptionRequest;
import com.tyrael.kharazim.application.prescription.vo.PagePrescriptionRequest;
import com.tyrael.kharazim.application.prescription.vo.PrescriptionExportVO;
import com.tyrael.kharazim.application.prescription.vo.PrescriptionVO;
import com.tyrael.kharazim.application.product.domain.ProductSku;
import com.tyrael.kharazim.application.product.mapper.ProductSkuMapper;
import com.tyrael.kharazim.application.settlement.event.CreateSettlementOrderEvent;
import com.tyrael.kharazim.application.skupublish.domain.SkuPublish;
import com.tyrael.kharazim.application.skupublish.mapper.SkuPublishMapper;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.excel.ExcelMergeStrategy;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionMapper prescriptionMapper;
    private final PrescriptionProductMapper prescriptionProductMapper;
    private final CustomerMapper customerMapper;
    private final ClinicMapper clinicMapper;
    private final CodeGenerator codeGenerator;
    private final ProductSkuMapper productSkuMapper;
    private final SkuPublishMapper skuPublishMapper;
    private final PrescriptionConverter prescriptionConverter;
    private final ApplicationEventPublisher publisher;
    private final PlatformTransactionManager transactionManager;
    private final InventoryService inventoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(CreatePrescriptionRequest request) {
        Prescription prescription = buildPrescription(request);
        this.save(prescription);

        // 预占库存
        this.occupy(prescription);

        // 创建结算单
        publisher.publishEvent(new CreateSettlementOrderEvent(this, prescription));

        return prescription.getCode();
    }

    private Prescription buildPrescription(CreatePrescriptionRequest request) {

        String customerCode = request.getCustomerCode();
        String clinicCode = request.getClinicCode();
        customerMapper.ensureCustomerExist(customerCode);
        Clinic clinic = clinicMapper.exactlyFindByCode(clinicCode);

        List<CreatePrescriptionRequest.Product> products = request.getProducts();
        Set<String> skuCodes = products.stream()
                .map(CreatePrescriptionRequest.Product::getSkuCode)
                .collect(Collectors.toSet());
        List<ProductSku> productSkus = productSkuMapper.listByCodes(skuCodes);
        Map<String, ProductSku> skuMap = productSkus.stream()
                .collect(Collectors.toMap(ProductSku::getCode, e -> e));

        List<SkuPublish> skuPublishes = skuPublishMapper.listClinicEffective(clinicCode, skuCodes);
        Map<String, SkuPublish> skuPublishMap = skuPublishes.stream()
                .collect(Collectors.toMap(SkuPublish::getSkuCode, e -> e));

        List<PrescriptionProduct> prescriptionProducts = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CreatePrescriptionRequest.Product product : products) {
            String skuCode = product.getSkuCode();
            ProductSku sku = skuMap.get(skuCode);
            DomainNotFoundException.assertFound(sku, skuCode);

            SkuPublish skuPublish = skuPublishMap.get(skuCode);
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
        prescription.setCode(codeGenerator.dailyNext(BusinessCodeConstants.PRESCRIPTION));
        prescription.setCustomerCode(customerCode);
        prescription.setClinicCode(clinicCode);
        prescription.setTotalAmount(totalAmount);
        prescription.setProducts(prescriptionProducts);
        prescription.setRemark(request.getRemark());

        return prescription;
    }

    private void save(Prescription prescription) {
        prescriptionMapper.insert(prescription);
        for (PrescriptionProduct product : prescription.getProducts()) {
            product.setPrescriptionCode(prescription.getCode());
        }
        prescriptionProductMapper.saveBatch(prescription.getProducts());
    }

    private void occupy(Prescription prescription) {
        List<InventoryOccupyCommand.Item> items = prescription.getProducts()
                .stream()
                .map(e -> new InventoryOccupyCommand.Item(e.getSkuCode(), e.getQuantity()))
                .toList();

        InventoryOccupyCommand occupyCommand = new InventoryOccupyCommand(
                prescription.getCode(),
                prescription.getClinicCode(),
                items,
                prescription.getCreator(),
                prescription.getCreatorCode());
        inventoryService.occupy(occupyCommand);
    }

    @Override
    public PrescriptionVO detail(String code) {
        Prescription prescription = prescriptionMapper.findByCode(code);
        DomainNotFoundException.assertFound(prescription, code);

        List<PrescriptionProduct> prescriptionProducts = prescriptionProductMapper.listByPrescriptionCode(code);
        return prescriptionConverter.prescriptionVO(prescription, prescriptionProducts);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PrescriptionVO> page(PagePrescriptionRequest pageRequest) {

        Page<Prescription> pageCondition = new Page<>(pageRequest.getPageIndex(), pageRequest.getPageSize());
        PageResponse<Prescription> pageData = prescriptionMapper.page(pageRequest, pageCondition);

        Collection<Prescription> prescriptions = pageData.getData();
        List<PrescriptionProduct> products = listPrescriptionProducts(prescriptions);

        return PageResponse.success(
                prescriptionConverter.prescriptionVOs(prescriptions, products),
                pageData.getTotalCount());
    }

    @Override
    @Transactional(readOnly = true)
    public void exportAll(HttpServletResponse response) throws IOException {
        WriteSheet writeSheet = EasyExcelFactory.writerSheet("处方数据")
                .head(PrescriptionExportVO.class)
                .registerWriteHandler(new ExcelMergeStrategy())
                .build();
        int pageSize = 200;
        try (ExcelWriter excelWriter = EasyExcelFactory.write(response.getOutputStream()).build();
             Cursor<Prescription> prescriptionCursor = prescriptionMapper.findAll()) {

            List<Prescription> slice = new ArrayList<>();
            for (Prescription prescription : prescriptionCursor) {
                slice.add(prescription);
                if (slice.size() >= pageSize) {
                    excelWriter.write(this.convertExports(slice), writeSheet);
                    slice = new ArrayList<>();
                }
            }
            excelWriter.write(this.convertExports(slice), writeSheet);

            response.addHeader("Content-disposition", "attachment;filename="
                    + URLEncoder.encode("全部处方数据.xlsx", StandardCharsets.UTF_8));
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        }
    }

    private List<PrescriptionExportVO> convertExports(List<Prescription> prescriptions) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        return transactionTemplate.execute(status -> {
            List<PrescriptionProduct> products = listPrescriptionProducts(prescriptions);
            return prescriptionConverter.prescriptionExportVOs(prescriptions, products);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public void export(PagePrescriptionRequest pageRequest, HttpServletResponse response) throws IOException {
        WriteSheet writeSheet = EasyExcelFactory.writerSheet("处方数据")
                .head(PrescriptionExportVO.class)
                .registerWriteHandler(new ExcelMergeStrategy())
                .build();
        int pageSize = 200;
        int pageNum = 1;
        try (ExcelWriter excelWriter = EasyExcelFactory.write(response.getOutputStream()).build()) {
            List<PrescriptionExportVO> exports;
            do {
                Page<Prescription> pageCondition = new Page<>(pageNum, pageSize, false);
                PageResponse<Prescription> pageData = prescriptionMapper.page(pageRequest, pageCondition);

                Collection<Prescription> prescriptions = pageData.getData();
                List<PrescriptionProduct> products = listPrescriptionProducts(prescriptions);

                exports = prescriptionConverter.prescriptionExportVOs(prescriptions, products);
                excelWriter.write(exports, writeSheet);

                pageNum++;
            } while (!exports.isEmpty());

            response.addHeader("Content-disposition", "attachment;filename="
                    + URLEncoder.encode("处方数据.xlsx", StandardCharsets.UTF_8));
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        }
    }

    private List<PrescriptionProduct> listPrescriptionProducts(Collection<Prescription> prescriptions) {
        List<String> prescriptionCodes = prescriptions.stream()
                .map(Prescription::getCode)
                .collect(Collectors.toList());
        return prescriptionProductMapper.listByPrescriptionCodes(prescriptionCodes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paidCallback(String code) {

        Prescription prescription = prescriptionMapper.findByCode(code);
        prescription.setProducts(prescriptionProductMapper.listByPrescriptionCode(code));

        // 处方已支付

        // 创建出库单
        publisher.publishEvent(new CreateOutboundOrderEvent(this, prescription));

    }

}
