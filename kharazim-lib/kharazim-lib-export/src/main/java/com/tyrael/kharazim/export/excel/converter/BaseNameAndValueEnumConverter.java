package com.tyrael.kharazim.export.excel.converter;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import com.tyrael.kharazim.base.dto.BaseHasNameEnum;

import java.util.Arrays;

/**
 * @author Tyrael Archangel
 * @since 2024/5/22
 */
public interface BaseNameAndValueEnumConverter<T extends Enum<T> & BaseHasNameEnum<T>> extends Converter<T> {

    @Override
    Class<T> supportJavaTypeKey();

    @Override
    default CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    default T convertToJavaData(ReadCellData<?> cellData,
                                ExcelContentProperty contentProperty,
                                GlobalConfiguration globalConfiguration) {
        String stringValue = cellData.getStringValue();
        if (stringValue == null || stringValue.isBlank()) {
            return null;
        }

        stringValue = stringValue.trim();
        T[] enumConstants = supportJavaTypeKey().getEnumConstants();
        for (T enumConstant : enumConstants) {
            if (stringValue.equalsIgnoreCase(enumConstant.getName()) || stringValue.equalsIgnoreCase(enumConstant.name())) {
                return enumConstant;
            }
        }

        throw new IllegalArgumentException("Unknown value '" + stringValue + "' accepted for " + Arrays.toString(enumConstants));
    }

    @Override
    default WriteCellData<String> convertToExcelData(T value,
                                                     ExcelContentProperty contentProperty,
                                                     GlobalConfiguration globalConfiguration) {
        if (value == null) {
            return new WriteCellData<>();
        }
        return new WriteCellData<>(value.getName());
    }

}
