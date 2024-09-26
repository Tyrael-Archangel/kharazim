package com.tyrael.kharazim.application.system.service.impl;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.config.DictCodeConstants;
import com.tyrael.kharazim.application.system.domain.Dict;
import com.tyrael.kharazim.application.system.domain.DictItem;
import com.tyrael.kharazim.application.system.dto.dict.*;
import com.tyrael.kharazim.application.system.mapper.DictItemMapper;
import com.tyrael.kharazim.application.system.mapper.DictMapper;
import com.tyrael.kharazim.application.system.service.DictService;
import com.tyrael.kharazim.common.dto.MultiResponse;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictServiceImpl implements DictService {

    private final DictMapper dictMapper;
    private final DictItemMapper dictItemMapper;

    @Override
    public PageResponse<DictDTO> pageDict(PageDictRequest pageDictRequest) {
        PageResponse<Dict> dictPage = dictMapper.page(pageDictRequest);
        List<DictDTO> dictionaries = dictPage.getData()
                .stream()
                .map(this::dictDTO)
                .toList();
        return PageResponse.success(dictionaries, dictPage.getTotalCount(), dictPage.getPageSize(), dictPage.getPageNum());
    }

    @Override
    public DictDTO dictDetail(Long id) {
        Dict dict = dictMapper.selectById(id);
        DomainNotFoundException.assertFound(dict, id);
        return this.dictDTO(dict);
    }

    private DictDTO dictDTO(Dict dict) {
        DictDTO dictDTO = new DictDTO();
        dictDTO.setId(dict.getId());
        dictDTO.setCode(dict.getCode());
        dictDTO.setName(dict.getName());
        dictDTO.setRemark(dict.getRemark());
        dictDTO.setStatusByEnable(dict.getEnabled());
        dictDTO.setCreator(dict.getCreator());
        dictDTO.setCreatorCode(dict.getCreatorCode());
        dictDTO.setCreateTime(dict.getCreateTime());
        dictDTO.setUpdater(dict.getUpdater());
        dictDTO.setUpdaterCode(dict.getUpdaterCode());
        dictDTO.setUpdateTime(dict.getUpdateTime());
        dictDTO.setAllowModifyItem(dict.getAllowModifyItem());
        return dictDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDict(SaveDictRequest addDictRequest, AuthUser currentUser) {

        Dict dict = createDict(addDictRequest, currentUser);

        try {
            dictMapper.insert(dict);
        } catch (DuplicateKeyException e) {
            log.warn("save dict error: {}", e.getMessage(), e);
            throw new BusinessException("字典编码已存在", e);
        }
    }

    private Dict createDict(SaveDictRequest addDictRequest, AuthUser currentUser) {
        Dict dict = new Dict();
        dict.setCode(addDictRequest.getCode());
        dict.setName(addDictRequest.getName());
        dict.setEnabled(addDictRequest.getEnable());
        dict.setRemark(addDictRequest.getRemark());
        dict.setSystemDict(Boolean.FALSE);
        dict.setAllowModifyItem(Boolean.TRUE);
        dict.setCreator(currentUser.getNickName());
        dict.setCreatorCode(currentUser.getCode());
        dict.setCreateTime(LocalDateTime.now());
        dict.setUpdater(currentUser.getNickName());
        dict.setUpdaterCode(currentUser.getCode());
        dict.setUpdateTime(LocalDateTime.now());
        return dict;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(Long dictId, SaveDictRequest modifyDictRequest, AuthUser currentUser) {
        Dict dict = dictMapper.selectById(dictId);
        DomainNotFoundException.assertFound(dict, dictId);

        if (dict.systemInnerDict()) {
            throw new ForbiddenException("系统内置字典不能修改");
        }

        String oldCode = dict.getCode();
        String newCode = modifyDictRequest.getCode();

        dict.setCode(newCode);
        dict.setName(modifyDictRequest.getName());
        dict.setEnabled(modifyDictRequest.getEnable());
        dict.setRemark(modifyDictRequest.getRemark());
        dict.setUpdater(currentUser.getNickName());
        dict.setUpdaterCode(currentUser.getCode());
        dict.setUpdateTime(LocalDateTime.now());

        try {
            dictMapper.updateById(dict);
        } catch (DuplicateKeyException e) {
            log.warn("update dict error: {}", e.getMessage(), e);
            throw new BusinessException("字典编码已存在", e);
        }

        if (!StringUtils.equals(oldCode, newCode)) {
            dictItemMapper.updateDictCode(oldCode, newCode);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<Dict> dicts = dictMapper.selectBatchIds(ids);
        Set<String> systemDicts = dicts.stream()
                .filter(Dict::systemInnerDict)
                .map(Dict::getName)
                .collect(Collectors.toSet());
        if (!systemDicts.isEmpty()) {
            throw new ForbiddenException("系统内置字典不能修改：" + String.join(", ", systemDicts));
        }
        if (dicts.isEmpty()) {
            return;
        }

        dictMapper.deleteBatchIds(ids);

        List<String> dictCodes = dicts.stream()
                .map(Dict::getCode)
                .toList();
        dictItemMapper.deleteByDictCodes(dictCodes);
    }

    @Override
    public MultiResponse<DictItemOptionDTO> listDictItem(String dictCode) {
        List<DictItem> dictItems = dictItemMapper.findByDictCode(dictCode);
        List<DictItemOptionDTO> dictItemOptions = dictItems.stream()
                .map(e -> new DictItemOptionDTO(e.getValue(), e.getName()))
                .toList();
        return MultiResponse.success(dictItemOptions);
    }

    @Override
    public List<DictItem> findByDictCode(String dictCode) {
        return dictItemMapper.findByDictCode(dictCode);
    }

    @Override
    public List<DictItem> findByDict(DictCodeConstants dictCodeConstant) {
        return this.findByDictCode(dictCodeConstant.getDictCode());
    }

    @Override
    public Map<String, String> dictItemMap(DictCodeConstants dictCodeConstant) {
        return this.findByDictCode(dictCodeConstant.getDictCode())
                .stream()
                .collect(Collectors.toMap(DictItem::getValue, DictItem::getName));
    }

    @Override
    public String findItemName(String dictCode, String dictItemValue) {
        return this.findByDictCode(dictCode)
                .stream()
                .filter(e -> StringUtils.equalsIgnoreCase(e.getValue(), dictItemValue))
                .findFirst()
                .map(DictItem::getName)
                .orElse(null);
    }

    @Override
    public DictItem findItem(String dictCode, String dictItemValue) {
        return this.findByDictCode(dictCode)
                .stream()
                .filter(e -> StringUtils.equalsIgnoreCase(e.getValue(), dictItemValue))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String findItemName(DictCodeConstants dictCodeConstants, String dictItemValue) {
        return findItemName(dictCodeConstants.getDictCode(), dictItemValue);
    }

    @Override
    public Set<String> findEnabledItems(String dictCode) {
        return dictItemMapper.findByDictCode(dictCode)
                .stream()
                .filter(DictItem::enable)
                .map(DictItem::getValue)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> findEnabledItems(DictCodeConstants dictCodeConstants) {
        return findEnabledItems(dictCodeConstants.getDictCode());
    }

    @Override
    public void ensureDictItemEnable(String dictCode, Collection<String> dictItemValues) {
        if (dictItemValues.isEmpty()) {
            return;
        }
        List<DictItem> enabledDictItems = dictItemMapper.findEnabledByDictCode(dictCode);
        if (enabledDictItems.isEmpty()) {
            throw new BusinessException("字典值无效："
                    + String.join(",", dictItemValues)
                    + "，没有可用字典值");
        }

        Set<String> enabledItemValues = enabledDictItems.stream()
                .map(DictItem::getValue)
                .collect(Collectors.toSet());

        List<String> disabledItemValues = dictItemValues.stream()
                .filter(e -> !enabledItemValues.contains(e))
                .toList();
        if (!disabledItemValues.isEmpty()) {
            throw new BusinessException("字典值无效：" + String.join(",", disabledItemValues)
                    + "，可用值：" + String.join(",", enabledItemValues));
        }

    }

    @Override
    public void ensureDictItemEnable(String dictCode, String dictItemValue) {
        this.ensureDictItemEnable(dictCode, Collections.singleton(dictItemValue));
    }

    @Override
    public void ensureDictItemEnable(DictCodeConstants dictCodeConstants, Collection<String> dictItemValues) {
        if (CollectionUtils.isEmpty(dictItemValues)) {
            return;
        }
        List<DictItem> enabledDictItems = dictItemMapper.findEnabledByDictCode(dictCodeConstants.getDictCode());
        String constantsDesc = Optional.ofNullable(dictCodeConstants.getDesc())
                .orElse("");
        if (enabledDictItems.isEmpty()) {
            throw new BusinessException(constantsDesc
                    + "字典值无效："
                    + String.join(",", dictItemValues)
                    + "，没有可用字典值");
        }

        Set<String> enabledItemValues = enabledDictItems.stream()
                .map(DictItem::getValue)
                .collect(Collectors.toSet());

        List<String> disabledItemValues = dictItemValues.stream()
                .filter(e -> !enabledItemValues.contains(e))
                .toList();
        if (!disabledItemValues.isEmpty()) {
            throw new BusinessException(constantsDesc
                    + "字典值无效：" + String.join(",", disabledItemValues)
                    + "，可用值：" + String.join(",", enabledItemValues));
        }
    }

    @Override
    public void ensureDictItemEnable(DictCodeConstants dictCodeConstants, String dictItemValue) {
        this.ensureDictItemEnable(dictCodeConstants, Collections.singleton(dictItemValue));
    }

    @Override
    public PageResponse<DictItemDTO> pageDictItem(PageDictItemRequest pageDictItemRequest) {
        PageResponse<DictItem> dictItemPage = dictItemMapper.page(pageDictItemRequest);
        List<DictItemDTO> dictionaries = dictItemPage.getData()
                .stream()
                .map(this::dictItemDTO)
                .toList();
        return PageResponse.success(dictionaries, dictItemPage.getTotalCount(), dictItemPage.getPageSize(), dictItemPage.getPageNum());
    }

    @Override
    public DictItemDTO dictItemDetail(Long dictItemId) {
        DictItem dictItem = dictItemMapper.selectById(dictItemId);
        DomainNotFoundException.assertFound(dictItem, dictItemId);
        return dictItemDTO(dictItem);
    }

    private DictItemDTO dictItemDTO(DictItem dictItem) {
        DictItemDTO dictItemDTO = new DictItemDTO();
        dictItemDTO.setId(dictItem.getId());
        dictItemDTO.setTypeCode(dictItem.getDictCode());
        dictItemDTO.setName(dictItem.getName());
        dictItemDTO.setValue(dictItem.getValue());
        dictItemDTO.setStatusByEnable(dictItem.getEnabled());
        dictItemDTO.setSort(dictItem.getSort());
        return dictItemDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDictItem(SaveDictItemRequest addDictItemRequest, AuthUser currentUser) {
        String dictCode = addDictItemRequest.getTypeCode();
        Dict dict = dictMapper.findByCode(dictCode);
        DomainNotFoundException.assertFound(dict, dictCode);

        if (dict.forbiddenModifyItem()) {
            throw new ForbiddenException("该字典不允许添加字典项");
        }

        DictItem dictItem = createDictItem(addDictItemRequest);
        try {
            dictItemMapper.insert(dictItem);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("字典项值重复", e);
        }

        dict.setUpdater(currentUser.getNickName());
        dict.setUpdaterCode(currentUser.getCode());
        dict.setUpdateTime(LocalDateTime.now());

        dictMapper.updateById(dict);
    }

    private DictItem createDictItem(SaveDictItemRequest addDictItemRequest) {
        DictItem dictItem = new DictItem();
        dictItem.setName(addDictItemRequest.getName());
        dictItem.setValue(addDictItemRequest.getValue());
        dictItem.setDictCode(addDictItemRequest.getTypeCode());
        dictItem.setSort(addDictItemRequest.getSort());
        dictItem.setEnabled(addDictItemRequest.getEnable());
        return dictItem;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyDictItem(Long dictItemId, SaveDictItemRequest modifyDictItemRequest, AuthUser currentUser) {
        DictItem dictItem = dictItemMapper.selectById(dictItemId);
        DomainNotFoundException.assertFound(dictItem, dictItemId);

        Dict dict = dictMapper.findByCode(dictItem.getDictCode());
        DomainNotFoundException.assertFound(dict, dictItem.getDictCode());

        if (dict.forbiddenModifyItem()) {
            throw new ForbiddenException("该字典不允许修改字典项");
        }

        dictItem.setName(modifyDictItemRequest.getName());
        dictItem.setValue(modifyDictItemRequest.getValue());
        dictItem.setEnabled(modifyDictItemRequest.getEnable());
        dictItem.setSort(modifyDictItemRequest.getSort());

        int updatedRows = dictItemMapper.updateById(dictItem);
        if (updatedRows > 0) {
            dict.setUpdater(currentUser.getNickName());
            dict.setUpdaterCode(currentUser.getCode());
            dict.setUpdateTime(LocalDateTime.now());

            dictMapper.updateById(dict);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteItem(List<Long> dictItemIds, AuthUser currentUser) {
        if (CollectionUtils.isEmpty(dictItemIds)) {
            return;
        }
        List<DictItem> dictItems = dictItemMapper.selectBatchIds(dictItemIds);
        if (dictItems.isEmpty()) {
            return;
        }

        Set<String> dictCodes = dictItems.stream()
                .map(DictItem::getDictCode)
                .collect(Collectors.toSet());
        List<String> forbiddenModifyDicts = dictMapper.listByCodes(dictCodes)
                .stream()
                .filter(Dict::forbiddenModifyItem)
                .map(Dict::getName)
                .toList();

        if (!forbiddenModifyDicts.isEmpty()) {
            throw new ForbiddenException("该字典不允许删除字典项：" + String.join(", ", forbiddenModifyDicts));
        }

        dictItemMapper.deleteBatchIds(dictItemIds);
        dictMapper.batchUpdateUpdater(dictCodes, currentUser.getNickName(), currentUser.getCode(), LocalDateTime.now());
    }

}
