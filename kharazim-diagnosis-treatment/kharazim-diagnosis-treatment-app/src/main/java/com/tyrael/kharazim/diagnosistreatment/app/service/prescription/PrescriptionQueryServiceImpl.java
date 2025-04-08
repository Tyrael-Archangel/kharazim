package com.tyrael.kharazim.diagnosistreatment.app.service.prescription;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcelFactory;
import cn.idev.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.diagnosistreatment.app.converter.PrescriptionConverter;
import com.tyrael.kharazim.diagnosistreatment.app.domain.prescription.Prescription;
import com.tyrael.kharazim.diagnosistreatment.app.domain.prescription.PrescriptionProduct;
import com.tyrael.kharazim.diagnosistreatment.app.mapper.PrescriptionMapper;
import com.tyrael.kharazim.diagnosistreatment.app.mapper.PrescriptionProductMapper;
import com.tyrael.kharazim.diagnosistreatment.app.vo.prescription.PagePrescriptionRequest;
import com.tyrael.kharazim.diagnosistreatment.app.vo.prescription.PrescriptionExportVO;
import com.tyrael.kharazim.diagnosistreatment.app.vo.prescription.PrescriptionVO;
import com.tyrael.kharazim.export.excel.ExcelMergeStrategy;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
@Service
@RequiredArgsConstructor
public class PrescriptionQueryServiceImpl implements PrescriptionQueryService {

    private final PrescriptionMapper prescriptionMapper;
    private final PrescriptionProductMapper prescriptionProductMapper;
    private final PrescriptionConverter prescriptionConverter;
    private final PlatformTransactionManager transactionManager;

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
        WriteSheet writeSheet = FastExcelFactory.writerSheet("处方数据")
                .head(PrescriptionExportVO.class)
                .registerWriteHandler(new ExcelMergeStrategy())
                .build();
        int pageSize = 200;
        try (ExcelWriter excelWriter = FastExcelFactory.write(response.getOutputStream()).build();
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
        WriteSheet writeSheet = FastExcelFactory.writerSheet("处方数据")
                .head(PrescriptionExportVO.class)
                .registerWriteHandler(new ExcelMergeStrategy())
                .build();
        int pageSize = 200;
        int pageNum = 1;
        try (ExcelWriter excelWriter = FastExcelFactory.write(response.getOutputStream()).build()) {
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

}
