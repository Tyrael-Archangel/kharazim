package com.tyrael.kharazim.diagnosistreatment.app.service.prescription;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcelFactory;
import cn.idev.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.diagnosistreatment.app.converter.PrescriptionConverter;
import com.tyrael.kharazim.diagnosistreatment.app.domain.prescription.Prescription;
import com.tyrael.kharazim.diagnosistreatment.app.domain.prescription.PrescriptionProduct;
import com.tyrael.kharazim.diagnosistreatment.app.mapper.PrescriptionMapper;
import com.tyrael.kharazim.diagnosistreatment.app.mapper.PrescriptionProductMapper;
import com.tyrael.kharazim.diagnosistreatment.app.vo.prescription.DailySalesModels;
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
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tyrael.kharazim.base.util.DateTimeUtil.endTimeOfDate;
import static com.tyrael.kharazim.base.util.DateTimeUtil.startTimeOfDate;

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

    @Override
    @Transactional(readOnly = true)
    public List<DailySalesModels.View> dailySalesTrends(DailySalesModels.FilterCommand filter) {
        return new DailySalesQueryProvider()
                .dailySales(filter.getClinicCode(), filter.getStart(), filter.getEnd());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<DailySalesModels.View> dailySalesPage(DailySalesModels.FilterCommand filter, PageCommand pageCommand) {
        return new DailySalesQueryProvider().page(filter, pageCommand);
    }

    @Override
    @Transactional(readOnly = true)
    public void dailySalesExport(DailySalesModels.FilterCommand filter, HttpServletResponse response) throws IOException {
        new DailySalesQueryProvider().export(filter, response);
    }

    private class DailySalesQueryProvider {

        PageResponse<DailySalesModels.View> page(DailySalesModels.FilterCommand filter, PageCommand pageCommand) {

            String clinicCode = filter.getClinicCode();
            LocalDate startDate = filter.getStart();
            LocalDate endDate = filter.getEnd();

            long totalItems = startDate.until(endDate, ChronoUnit.DAYS);
            if (totalItems <= 0) {
                return PageResponse.success(List.of(), 0);
            }

            Integer pageIndex = pageCommand.getPageIndex();
            Integer pageSize = pageCommand.getPageSize();

            LocalDate pageBegin = startDate.plusDays((pageIndex - 1L) * pageSize);
            LocalDate pageEnd = pageBegin.plusDays(pageSize - 1L);
            if (pageEnd.isAfter(endDate)) {
                pageEnd = endDate;
            }

            List<DailySalesModels.View> views = dailySales(clinicCode, pageBegin, pageEnd);
            return PageResponse.success(views, (int) totalItems);
        }

        void export(DailySalesModels.FilterCommand filter, HttpServletResponse response) throws IOException {

            String clinicCode = filter.getClinicCode();
            LocalDate queryBegin = filter.getStart();
            LocalDate queryEnd = filter.getEnd();

            int pageSize = 50;

            WriteSheet writeSheet = FastExcelFactory.writerSheet("daily-sales")
                    .head(DailySalesModels.View.class)
                    .build();

            try (ExcelWriter excelWriter = FastExcelFactory.write(response.getOutputStream()).build()) {

                LocalDate begin = queryBegin;

                List<DailySalesModels.View> exports;
                do {

                    LocalDate end = begin.plusDays(pageSize - 1);
                    if (end.isAfter(queryEnd)) {
                        end = queryEnd;
                    }

                    exports = dailySales(clinicCode, begin, end);
                    excelWriter.write(exports, writeSheet);

                    begin = begin.plusDays(pageSize);
                } while (!exports.isEmpty());

                response.addHeader("Content-disposition", "attachment;filename="
                        + URLEncoder.encode("daily_sales.xlsx", StandardCharsets.UTF_8));
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            }
        }

        List<DailySalesModels.View> dailySales(String clinicCode, LocalDate startDate, LocalDate endDate) {

            List<DailySalesModels.View> dailySales = prescriptionMapper.dailySales(
                    clinicCode, startTimeOfDate(startDate), endTimeOfDate(endDate));
            Map<LocalDate, DailySalesModels.View> dailySalesMap = dailySales.stream()
                    .collect(Collectors.toMap(DailySalesModels.View::getDate, e -> e));

            List<DailySalesModels.View> result = new ArrayList<>();
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                DailySalesModels.View view = dailySalesMap.get(date);
                if (view == null) {
                    view = new DailySalesModels.View(date, BigDecimal.ZERO);
                }
                result.add(view);
            }

            return result;
        }

    }

}
