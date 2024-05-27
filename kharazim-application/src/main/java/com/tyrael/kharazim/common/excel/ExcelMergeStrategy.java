package com.tyrael.kharazim.common.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.handler.context.RowWriteHandlerContext;
import com.alibaba.excel.write.property.ExcelWriteHeadProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/5/24
 */
public class ExcelMergeStrategy implements RowWriteHandler {

    private final String mergeCellName = MergeCell.class.getName();
    private final Map<Class<?>, MergeInfo> headCache = new HashMap<>();
    private MergeUnique mergeUnique;

    @Override
    public void afterRowDispose(RowWriteHandlerContext context) {
        Integer rowIndex = context.getRowIndex();
        if (!Boolean.FALSE.equals(context.getHead()) || rowIndex == null) {
            return;
        }

        ExcelWriteHeadProperty writeProperty = context.getWriteSheetHolder().getExcelWriteHeadProperty();
        MergeInfo mergeInfo = headCache.computeIfAbsent(writeProperty.getHeadClazz(), k -> getMergeInfo(writeProperty));
        if (mergeInfo instanceof EmptyMergeInfo) {
            return;
        }

        Cell currentCell = context.getRow()
                .getCell(mergeInfo.getUniqueIndex());
        String currentRowUniqueValue = currentCell.toString();

        if (mergeUnique == null) {
            mergeUnique = new MergeUnique(currentRowUniqueValue, rowIndex);
            return;
        }

        Integer fromIndex = mergeUnique.fromIndex;
        if (!StringUtils.equals(mergeUnique.uniqueValue, currentRowUniqueValue)) {
            for (Integer mergeColumnIndex : mergeInfo.getMergeColumnIndices()) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(
                        fromIndex, rowIndex - 1, mergeColumnIndex, mergeColumnIndex);
                context.getWriteSheetHolder().getSheet().addMergedRegionUnsafe(cellRangeAddress);
                Cell cell = context.getWriteSheetHolder().getSheet().getRow(fromIndex).getCell(mergeColumnIndex);
                Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                CellStyle style = workbook.createCellStyle();
                style.cloneStyleFrom(currentCell.getCellStyle());
                // 设置垂直居中
                style.setVerticalAlignment(VerticalAlignment.CENTER);
                cell.setCellStyle(style);
            }

            mergeUnique = new MergeUnique(currentRowUniqueValue, rowIndex);
        }
    }

    private MergeInfo getMergeInfo(ExcelWriteHeadProperty excelWriteHeadProperty) {
        Class<?> headClass = excelWriteHeadProperty.getHeadClazz();
        Field[] declaredFields = headClass.getDeclaredFields();
        Map<Field, Integer> fieldColumnIndexMap = excelWriteHeadProperty.getHeadMap()
                .values()
                .stream()
                .collect(Collectors.toMap(Head::getField, Head::getColumnIndex));
        List<Integer> mergeColumnIndices = new ArrayList<>();
        Integer uniqueIndex = null;
        for (Field declaredField : declaredFields) {
            MergeCell mergeCell = declaredField.getAnnotation(MergeCell.class);
            if (mergeCell != null) {
                Integer mergeColumnIndex = fieldColumnIndexMap.get(declaredField);
                if (mergeColumnIndex == null) {
                    throw new IllegalArgumentException(mergeCellName + " must use with a excel column");
                }
                if (mergeCell.unique()) {
                    if (uniqueIndex == null) {
                        uniqueIndex = mergeColumnIndex;
                    } else {
                        throw new IllegalArgumentException("there is only one column can marked as " + mergeCellName + ".unique");
                    }
                }
                mergeColumnIndices.add(mergeColumnIndex);
            }
        }
        if (mergeColumnIndices.isEmpty()) {
            return new EmptyMergeInfo(headClass);
        }

        if (uniqueIndex == null && mergeColumnIndices.size() == 1) {
            uniqueIndex = mergeColumnIndices.get(0);
        }
        if (uniqueIndex == null) {
            throw new IllegalArgumentException("a " + mergeCellName + ".unique required");
        }
        return new MergeInfo(headClass, mergeColumnIndices, uniqueIndex);
    }

    @Getter
    @AllArgsConstructor
    private static class MergeInfo {
        private final Class<?> headClass;
        private final List<Integer> mergeColumnIndices;
        private final Integer uniqueIndex;
    }

    private static class EmptyMergeInfo extends MergeInfo {
        private EmptyMergeInfo(Class<?> headClass) {
            super(headClass, Collections.emptyList(), null);
        }
    }

    private record MergeUnique(String uniqueValue, Integer fromIndex) {
    }

}
