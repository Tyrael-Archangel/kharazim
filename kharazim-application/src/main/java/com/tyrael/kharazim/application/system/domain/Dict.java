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
public sealed class Dict permits Dict.EnumDict {

    private final String code;
    private final String desc;

    @ToString
    public static final class EnumDict<T extends Enum<T> & BaseHasNameEnum<T>> extends Dict {

        private final Class<T> relatedEnum;
        private final Map<String, String> items;

        EnumDict(String code, Class<T> relatedEnum, String desc) {
            super(code, desc);
            this.relatedEnum = relatedEnum;
            this.items = Arrays.stream(this.relatedEnum.getEnumConstants())
                    .collect(Collectors.toMap(Enum::name, BaseHasNameEnum::getName, (e1, e2) -> e1, LinkedHashMap::new));
        }

        public String getItemValue(String itemKey) {
            return items.get(itemKey);
        }

        public Map<String, String> getDictItems() {
            return new LinkedHashMap<>(items);
        }
    }

}
