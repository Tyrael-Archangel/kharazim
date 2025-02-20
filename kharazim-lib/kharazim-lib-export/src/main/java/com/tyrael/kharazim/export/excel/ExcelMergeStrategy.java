package com.tyrael.kharazim.export.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.handler.WorkbookWriteHandler;
import com.alibaba.excel.write.handler.context.RowWriteHandlerContext;
import com.alibaba.excel.write.handler.context.WorkbookWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.property.ExcelWriteHeadProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/5/24
 */
public class ExcelMergeStrategy implements RowWriteHandler, WorkbookWriteHandler {

    private final String mergeCellName = MergeCell.class.getName();
    private final Map<Class<?>, MergeInfo> headCache = new HashMap<>();
    private MergeUnique mergeUnique;

    @Override
    public void afterRowDispose(RowWriteHandlerContext context) {
        Integer rowIndex = context.getRowIndex();
        if (!Boolean.FALSE.equals(context.getHead()) || rowIndex == null) {
            return;
        }

        WriteSheetHolder writeSheetHolder = context.getWriteSheetHolder();
        ExcelWriteHeadProperty writeProperty = writeSheetHolder.getExcelWriteHeadProperty();
        MergeInfo mergeInfo = headCache.computeIfAbsent(writeProperty.getHeadClazz(), k -> getMergeInfo(writeProperty));
        if (mergeInfo instanceof EmptyMergeInfo) {
            return;
        }

        Cell currentCell = context.getRow().getCell(mergeInfo.getUniqueIndex());
        String currentRowUniqueValue = currentCell.toString();

        if (mergeUnique == null) {
            mergeUnique = new MergeUnique(currentRowUniqueValue, rowIndex);
            return;
        }

        if (!StringUtils.equals(mergeUnique.uniqueValue, currentRowUniqueValue)) {

            this.mergeRowCells(writeSheetHolder, mergeInfo, mergeUnique.fromIndex, rowIndex - 1);

            mergeUnique = new MergeUnique(currentRowUniqueValue, rowIndex);
        }
    }

    @Override
    public void afterWorkbookDispose(WorkbookWriteHandlerContext context) {
        if (mergeUnique == null) {
            return;
        }

        WriteSheetHolder writeSheetHolder = context.getWriteContext().writeSheetHolder();
        ExcelWriteHeadProperty writeProperty = writeSheetHolder.getExcelWriteHeadProperty();
        MergeInfo mergeInfo = headCache.computeIfAbsent(writeProperty.getHeadClazz(), k -> getMergeInfo(writeProperty));
        if (mergeInfo instanceof EmptyMergeInfo) {
            return;
        }

        this.mergeRowCells(writeSheetHolder, mergeInfo, mergeUnique.fromIndex, writeSheetHolder.getLastRowIndex());
    }

    private void mergeRowCells(WriteSheetHolder writeSheetHolder, MergeInfo mergeInfo, int fromRow, int endRow) {
        if (fromRow >= endRow) {
            return;
        }

        for (Integer columnIndex : mergeInfo.getMergeColumnIndices()) {
            Sheet sheet = writeSheetHolder.getSheet();
            sheet.addMergedRegionUnsafe(new CellRangeAddress(fromRow, endRow, columnIndex, columnIndex));
            sheet.getRow(fromRow)
                    .getCell(columnIndex)
                    .getCellStyle()
                    .setVerticalAlignment(VerticalAlignment.CENTER);
        }
    }

    private MergeInfo getMergeInfo(ExcelWriteHeadProperty excelWriteHeadProperty) {
        Class<?> headClass = excelWriteHeadProperty.getHeadClazz();
        if (headClass == null) {
            return new EmptyMergeInfo();
        }
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
            return new EmptyMergeInfo();
        }

        if (uniqueIndex == null && mergeColumnIndices.size() == 1) {
            uniqueIndex = mergeColumnIndices.get(0);
        }
        if (uniqueIndex == null) {
            throw new IllegalArgumentException("a " + mergeCellName + ".unique required");
        }
        return new MergeInfo(mergeColumnIndices, uniqueIndex);
    }

    @Getter
    @AllArgsConstructor
    private static class MergeInfo {
        private final List<Integer> mergeColumnIndices;
        private final Integer uniqueIndex;
    }

    private static class EmptyMergeInfo extends MergeInfo {
        private EmptyMergeInfo() {
            super(Collections.emptyList(), null);
        }
    }

    private record MergeUnique(String uniqueValue, Integer fromIndex) {
    }

}
