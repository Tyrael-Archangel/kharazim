package com.tyrael.kharazim.application.system.service.impl;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.system.domain.DictConstant;
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
        List<DictConstant> dictConstants = DictConstants.allDictConstants();
        List<DictConstant> allMatchedDictConstants = dictConstants.stream()
                .filter(e -> matchQuery(pageRequest, e))
                .toList();

        List<DictDTO> dictPage = allMatchedDictConstants.stream()
                .skip((pageRequest.getPageIndex() - 1L) * pageRequest.getPageSize())
                .limit(pageRequest.getPageSize())
                .map(this::dictDTO)
                .toList();
        return PageResponse.success(dictPage, allMatchedDictConstants.size(), pageRequest.getPageSize(), pageRequest.getPageIndex());
    }

    private boolean matchQuery(PageDictRequest pageDictRequest, DictConstant dictConstant) {
        String keywords = pageDictRequest.getKeywords();
        boolean match = true;
        if (StringUtils.isNotBlank(keywords)) {
            match = (StringUtils.contains(dictConstant.getCode(), keywords)
                    || StringUtils.contains(dictConstant.getDesc(), keywords));
        }
        return match;
    }

    private DictDTO dictDTO(DictConstant dictConstant) {
        DictDTO dictDTO = new DictDTO();
        dictDTO.setCode(dictConstant.getCode());
        dictDTO.setDesc(dictConstant.getDesc());
        dictDTO.setAllowModifyItem(!(dictConstant instanceof DictConstant.EnumDictConstant));
        return dictDTO;
    }

    @Override
    public List<DictItemDTO> listItems(String dictCode) {
        DictConstant dictConstant = DictConstants.getDictConstant(dictCode);
        DomainNotFoundException.assertFound(dictConstant, dictCode);

        if (dictConstant instanceof DictConstant.EnumDictConstant<?> enumDictConstant) {
            AtomicInteger sort = new AtomicInteger(1);
            return enumDictConstant.getDictItemMap().entrySet()
                    .stream()
                    .map(itemValueToName -> DictItemDTO.builder()
                            .dictCode(dictCode)
                            .key(itemValueToName.getKey())
                            .value(itemValueToName.getValue())
                            .sort(sort.getAndIncrement())
                            .build())
                    .toList();
        } else {

            return dictItemMapper.listByDictCode(dictConstant.getCode())
                    .stream()
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
    public Map<String, String> dictItemMap(DictConstant dictConstant) {
        if (dictConstant instanceof DictConstant.EnumDictConstant<?> enumDictConstant) {
            return enumDictConstant.getDictItemMap();
        }
        return dictItemMapper.listByDictCode(dictConstant.getCode())
                .stream()
                .collect(Collectors.toMap(DictItem::getValue, DictItem::getKey));
    }

    @Override
    public String findItemValue(DictConstant dictConstant, String dictItemKey) {
        if (dictConstant instanceof DictConstant.EnumDictConstant<?> enumDictConstant) {
            return enumDictConstant.getItemName(dictItemKey);
        } else {
            DictItem dictItem = dictItemMapper.finByDictCodeAndItemKey(dictConstant.getCode(), dictItemKey);
            return dictItem == null ? null : dictItem.getKey();
        }
    }

    @Override
    public Set<String> dictItemKeys(DictConstant dictConstant) {
        if (dictConstant instanceof DictConstant.EnumDictConstant<?> enumDictConstant) {
            return enumDictConstant.getDictItemMap().keySet();
        } else {
            return dictItemMapper.listByDictCode(dictConstant.getCode())
                    .stream()
                    .map(DictItem::getKey)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public void ensureDictItemEnable(DictConstant dictConstant, Collection<String> dictItemKeys) {
        if (CollectionUtils.isEmpty(dictItemKeys)) {
            return;
        }

        Set<String> availableItemKeys = dictItemKeys(dictConstant);

        String constantsDesc = Optional.ofNullable(dictConstant.getDesc())
                .orElse("");
        if (availableItemKeys.isEmpty()) {
            throw new BusinessException(constantsDesc
                    + "字典值无效：" + String.join(",", dictItemKeys)
                    + "，没有可用字典值");
        }

        List<String> unavailableKeys = dictItemKeys.stream()
                .filter(e -> !availableItemKeys.contains(e))
                .toList();
        if (!unavailableKeys.isEmpty()) {
            throw new BusinessException(constantsDesc
                    + "字典值无效：" + String.join(",", unavailableKeys)
                    + "，可用值：" + String.join(",", availableItemKeys));
        }
    }

    @Override
    public void ensureDictItemEnable(DictConstant dictConstant, String dictItemKey) {
        this.ensureDictItemEnable(dictConstant, Collections.singleton(dictItemKey));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDictItem(SaveDictItemRequest addDictItemRequest, AuthUser currentUser) {
        String dictCode = addDictItemRequest.getDictCode();
        DictConstant dictConstant = DictConstants.getDictConstant(dictCode);
        DomainNotFoundException.assertFound(dictConstant, dictCode);
        if (dictConstant instanceof DictConstant.EnumDictConstant<?>) {
            throw new ForbiddenException("该字典不允许添加字典项");
        }

        DictItem dictItem = createDictItem(addDictItemRequest);
        try {
            dictItemMapper.insert(dictItem);
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
        DictConstant dictConstant = DictConstants.getDictConstant(dictCode);
        DomainNotFoundException.assertFound(dictConstant, dictCode);
        if (dictConstant instanceof DictConstant.EnumDictConstant<?>) {
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
