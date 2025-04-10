package com.tyrael.kharazim.basicdata.sdk.service;

import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.sdk.model.DictConstant;
import com.tyrael.kharazim.basicdata.sdk.model.DictItemVO;
import com.tyrael.kharazim.basicdata.sdk.model.DictVO;
import com.tyrael.kharazim.basicdata.sdk.model.InitDictRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2025/2/21
 */
public interface DictServiceApi {

    /**
     * list dict items by code
     *
     * @param dictCode dictCode
     * @return dict items
     */
    List<DictItemVO> listItems(String dictCode);

    /**
     * list dict item values by dictCodeConstants
     *
     * @param dict {@link DictConstant}
     * @return dict item codes
     */
    default Set<String> dictItemKeys(DictConstant dict) {
        return listItems(dict)
                .stream()
                .map(DictItemVO::getKey)
                .collect(Collectors.toSet());
    }

    /**
     * list dict items by DictConstant
     *
     * @param dict DictConstant
     * @return dict items
     */
    default List<DictItemVO> listItems(DictConstant dict) {
        return listItems(dict.getCode());
    }

    /**
     * add dict item
     *
     * @param dictItem {@link DictItemVO}
     */
    void addDictItem(DictItemVO dictItem);

    /**
     * init system dict
     *
     * @param initDictRequest {@link InitDictRequest}
     */
    void initSystemDict(InitDictRequest initDictRequest);

    /**
     * init DictConstants
     *
     * @param dictConstants DictConstants
     */
    default void init(DictConstant... dictConstants) {
        for (DictConstant dictionary : dictConstants) {
            initDict(dictionary);
        }
    }

    /**
     * init DictConstants
     *
     * @param dict DictConstant
     */
    default void initDict(DictConstant dict) {

        DictVO dictVO;
        List<DictItemVO> dictItems;

        if (dict instanceof DictConstant.EnumDict<?> enumDict) {
            dictVO = new DictVO(dict.getCode(), dict.getDesc(), false);
            AtomicInteger sort = new AtomicInteger(1);
            dictItems = enumDict.getDictItems().entrySet()
                    .stream()
                    .map(entry -> new DictItemVO(
                            dictVO.getDesc(),
                            dictVO.getCode(),
                            entry.getKey(),
                            entry.getValue(),
                            sort.getAndIncrement()))
                    .toList();
        } else {
            dictVO = new DictVO(dict.getCode(), dict.getDesc(), true);
            dictItems = Collections.emptyList();
        }

        initSystemDict(new InitDictRequest(dictVO, dictItems));
    }

    /**
     * 验证字典值是否有效
     *
     * @param dictCode     字典编码
     * @param dictItemKeys 字典值
     */
    default void ensureDictItemEnable(String dictCode, Collection<String> dictItemKeys) {
        if (CollectionUtils.isEmpty(dictItemKeys)) {
            return;
        }

        List<DictItemVO> dictItems = listItems(dictCode);
        Set<String> availableItemKeys = dictItems.stream()
                .map(DictItemVO::getKey)
                .collect(Collectors.toSet());

        String constantsDesc = dictItems.stream()
                .map(DictItemVO::getDictDesc)
                .filter(StringUtils::isNotBlank)
                .findAny()
                .orElse("");
        if (availableItemKeys.isEmpty()) {
            throw new BusinessException(constantsDesc
                    + "字典项键无效：" + String.join(",", dictItemKeys)
                    + "，没有可用字典键值");
        }

        List<String> unavailableKeys = dictItemKeys.stream()
                .filter(e -> !availableItemKeys.contains(e))
                .toList();
        if (!unavailableKeys.isEmpty()) {
            throw new BusinessException(constantsDesc
                    + "字典项键无效：" + String.join(",", unavailableKeys)
                    + "，可用值：" + String.join(",", availableItemKeys));
        }
    }

    /**
     * 验证字典值是否有效
     *
     * @param dict         {@link DictConstant}
     * @param dictItemKeys 字典值
     */
    default void ensureDictItemEnable(DictConstant dict, Collection<String> dictItemKeys) {
        ensureDictItemEnable(dict.getCode(), dictItemKeys);
    }

    /**
     * 验证字典值是否有效
     *
     * @param dict        {@link DictConstant}
     * @param dictItemKey 字典键
     */
    default void ensureDictItemEnable(DictConstant dict, String dictItemKey) {
        ensureDictItemEnable(dict, List.of(dictItemKey));
    }

    /**
     * 字典项 key -> value
     *
     * @param dictCode 字典编码
     * @return 字典项 key -> value
     */
    default Map<String, String> dictItemMap(String dictCode) {
        return listItems(dictCode)
                .stream()
                .collect(Collectors.toMap(DictItemVO::getKey, DictItemVO::getValue));
    }

    /**
     * 字典项 key -> value
     *
     * @param dict {@link DictConstant}
     * @return 字典项 key -> value
     */
    default Map<String, String> dictItemMap(DictConstant dict) {
        return dictItemMap(dict.getCode());
    }

    /**
     * 查询字典项值
     *
     * @param dictCode    字典编码
     * @param dictItemKey 字典项键
     * @return 字典项名称
     */
    default String findItemValue(String dictCode, String dictItemKey) {
        return dictItemMap(dictCode).get(dictItemKey);
    }

    /**
     * 查询字典项值
     *
     * @param dict        {@link DictConstant}
     * @param dictItemKey 字典项键
     * @return 字典项名称
     */
    default String findItemValue(DictConstant dict, String dictItemKey) {
        return findItemValue(dict.getCode(), dictItemKey);
    }

}
