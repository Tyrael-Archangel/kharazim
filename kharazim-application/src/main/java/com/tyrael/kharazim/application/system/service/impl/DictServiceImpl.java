package com.tyrael.kharazim.application.system.service.impl;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.system.domain.Dict;
import com.tyrael.kharazim.application.system.domain.DictConstants;
import com.tyrael.kharazim.application.system.domain.DictItem;
import com.tyrael.kharazim.application.system.dto.dict.DictDTO;
import com.tyrael.kharazim.application.system.dto.dict.DictItemDTO;
import com.tyrael.kharazim.application.system.dto.dict.PageDictRequest;
import com.tyrael.kharazim.application.system.dto.dict.SaveDictItemRequest;
import com.tyrael.kharazim.application.system.mapper.DictItemMapper;
import com.tyrael.kharazim.application.system.service.DictService;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import com.tyrael.kharazim.common.exception.ForbiddenException;
import com.tyrael.kharazim.common.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictServiceImpl implements DictService {

    private final DictItemMapper dictItemMapper;

    @Override
    public PageResponse<DictDTO> pageDict(PageDictRequest pageRequest) {
        List<Dict> dictionaries = DictConstants.allDictConstants();
        List<Dict> allMatchedDictionaris = dictionaries.stream()
                .filter(e -> matchQuery(pageRequest, e))
                .toList();

        List<DictDTO> dictPage = allMatchedDictionaris.stream()
                .skip((pageRequest.getPageIndex() - 1L) * pageRequest.getPageSize())
                .limit(pageRequest.getPageSize())
                .map(this::dictDTO)
                .toList();
        return PageResponse.success(dictPage, allMatchedDictionaris.size());
    }

    private boolean matchQuery(PageDictRequest pageDictRequest, Dict dict) {
        String keywords = pageDictRequest.getKeywords();
        boolean match = true;
        if (StringUtils.isNotBlank(keywords)) {
            match = (StringUtils.contains(dict.getCode(), keywords)
                    || StringUtils.contains(dict.getDesc(), keywords));
        }
        return match;
    }

    private DictDTO dictDTO(Dict dict) {
        DictDTO dictDTO = new DictDTO();
        dictDTO.setCode(dict.getCode());
        dictDTO.setDesc(dict.getDesc());
        dictDTO.setAllowModifyItem(!(dict instanceof Dict.EnumDict));
        return dictDTO;
    }

    @Override
    public List<DictItemDTO> listItems(String dictCode) {
        Dict dict = DictConstants.getDictConstant(dictCode);
        DomainNotFoundException.assertFound(dict, dictCode);

        if (dict instanceof Dict.EnumDict<?> enumDictConstant) {
            AtomicInteger sort = new AtomicInteger(1);
            return enumDictConstant.getDictItems()
                    .entrySet()
                    .stream()
                    .map(itemValueToName -> DictItemDTO.builder()
                            .dictCode(dictCode)
                            .key(itemValueToName.getKey())
                            .value(itemValueToName.getValue())
                            .sort(sort.getAndIncrement())
                            .build())
                    .toList();
        } else {

            return dictItemMapper.listByDictCode(dict.getCode())
                    .stream()
                    .sorted(Comparator.comparing(DictItem::getSort).thenComparing(DictItem::getId))
                    .map(e -> DictItemDTO.builder()
                            .id(e.getId())
                            .dictCode(e.getDictCode())
                            .key(e.getKey())
                            .value(e.getValue())
                            .sort(e.getSort())
                            .build())
                    .toList();
        }
    }

    @Override
    public Map<String, String> dictItemMap(Dict dict) {
        if (dict instanceof Dict.EnumDict<?> enumDictConstant) {
            return enumDictConstant.getDictItems();
        }
        return dictItemMapper.listByDictCode(dict.getCode())
                .stream()
                .collect(Collectors.toMap(DictItem::getKey, DictItem::getValue));
    }

    @Override
    public String findItemValue(Dict dict, String dictItemKey) {
        if (dict instanceof Dict.EnumDict<?> enumDictConstant) {
            return enumDictConstant.getItemValue(dictItemKey);
        } else {
            DictItem dictItem = dictItemMapper.finByDictCodeAndItemKey(dict.getCode(), dictItemKey);
            return dictItem == null ? null : dictItem.getValue();
        }
    }

    @Override
    public Set<String> dictItemKeys(Dict dict) {
        if (dict instanceof Dict.EnumDict<?> enumDictConstant) {
            return enumDictConstant.getDictItems().keySet();
        } else {
            return dictItemMapper.listByDictCode(dict.getCode())
                    .stream()
                    .map(DictItem::getKey)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public void ensureDictItemEnable(Dict dict, Collection<String> dictItemKeys) {
        if (CollectionUtils.isEmpty(dictItemKeys)) {
            return;
        }

        Set<String> availableItemKeys = dictItemKeys(dict);

        String constantsDesc = Optional.ofNullable(dict.getDesc())
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

    @Override
    public void ensureDictItemEnable(Dict dict, String dictItemKey) {
        this.ensureDictItemEnable(dict, Collections.singleton(dictItemKey));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addDictItem(SaveDictItemRequest addDictItemRequest, AuthUser currentUser) {
        String dictCode = addDictItemRequest.getDictCode();
        Dict dict = DictConstants.getDictConstant(dictCode);
        DomainNotFoundException.assertFound(dict, dictCode);
        if (dict instanceof Dict.EnumDict<?>) {
            throw new ForbiddenException("该字典不允许添加字典项");
        }

        DictItem dictItem = createDictItem(addDictItemRequest);
        try {
            dictItemMapper.insert(dictItem);
            return dictItem.getId();
        } catch (DuplicateKeyException e) {
            throw new BusinessException("字典项键重复", e);
        }

    }

    private DictItem createDictItem(SaveDictItemRequest addDictItemRequest) {
        DictItem dictItem = new DictItem();
        dictItem.setKey(addDictItemRequest.getKey());
        dictItem.setValue(addDictItemRequest.getValue());
        dictItem.setDictCode(addDictItemRequest.getDictCode());
        dictItem.setSort(addDictItemRequest.getSort());
        return dictItem;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyDictItem(Long dictItemId, SaveDictItemRequest modifyDictItemRequest, AuthUser currentUser) {
        DictItem dictItem = dictItemMapper.selectById(dictItemId);
        DomainNotFoundException.assertFound(dictItem, dictItemId);

        String dictCode = dictItem.getDictCode();
        Dict dict = DictConstants.getDictConstant(dictCode);
        DomainNotFoundException.assertFound(dict, dictCode);
        if (dict instanceof Dict.EnumDict<?>) {
            throw new ForbiddenException("该字典不允许修改字典项");
        }

        dictItem.setKey(modifyDictItemRequest.getKey());
        dictItem.setValue(modifyDictItemRequest.getValue());
        dictItem.setSort(modifyDictItemRequest.getSort());

        try {
            dictItemMapper.updateById(dictItem);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("字典项键重复", e);
        }
    }

}
