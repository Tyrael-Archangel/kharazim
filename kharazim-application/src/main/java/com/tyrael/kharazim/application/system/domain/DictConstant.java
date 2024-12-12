package com.tyrael.kharazim.application.system.domain;

import com.tyrael.kharazim.common.dto.BaseHasNameEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/12/11
 */
@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public sealed class DictConstant permits DictConstant.EnumDictConstant {

    private final String code;
    private final String desc;

    @ToString
    public static final class EnumDictConstant<T extends Enum<T> & BaseHasNameEnum<T>> extends DictConstant {

        private final Class<T> relatedEnum;
        private final Map<String, String> itemMap;

        EnumDictConstant(String code, String desc, Class<T> relatedEnum) {
            super(code, desc);
            this.relatedEnum = relatedEnum;
            this.itemMap = Arrays.stream(relatedEnum.getEnumConstants())
                    .collect(Collectors.toMap(Enum::name, BaseHasNameEnum::getName));
        }

        public String getItemName(String itemKey) {
            return itemMap.get(itemKey);
        }

        public Map<String, String> getDictItemMap() {
            return new LinkedHashMap<>(itemMap);
        }
    }

}
