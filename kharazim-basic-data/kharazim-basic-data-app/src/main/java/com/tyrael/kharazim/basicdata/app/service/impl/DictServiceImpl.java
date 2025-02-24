package com.tyrael.kharazim.basicdata.app.service.impl;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.base.exception.ForbiddenException;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.app.domain.Dict;
import com.tyrael.kharazim.basicdata.app.domain.DictItem;
import com.tyrael.kharazim.basicdata.app.dto.dict.DictDTO;
import com.tyrael.kharazim.basicdata.app.dto.dict.DictItemDTO;
import com.tyrael.kharazim.basicdata.app.dto.dict.PageDictRequest;
import com.tyrael.kharazim.basicdata.app.dto.dict.SaveDictItemRequest;
import com.tyrael.kharazim.basicdata.app.mapper.DictItemMapper;
import com.tyrael.kharazim.basicdata.app.mapper.DictMapper;
import com.tyrael.kharazim.basicdata.app.service.DictService;
import com.tyrael.kharazim.basicdata.model.DictItemVO;
import com.tyrael.kharazim.basicdata.model.DictVO;
import com.tyrael.kharazim.basicdata.model.InitDictRequest;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

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
    public PageResponse<DictDTO> pageDict(PageDictRequest pageRequest) {
        List<Dict> dictionaries = dictMapper.all();
        List<Dict> allMatchedDictionaries = dictionaries.stream()
                .filter(e -> matchQuery(pageRequest, e))
                .toList();

        List<DictDTO> dictPage = allMatchedDictionaries.stream()
                .skip((pageRequest.getPageIndex() - 1L) * pageRequest.getPageSize())
                .limit(pageRequest.getPageSize())
                .map(this::dictDTO)
                .toList();
        return PageResponse.success(dictPage, allMatchedDictionaries.size());
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
        dictDTO.setAllowModifyItem(dict.getAllowModifyItem());
        return dictDTO;
    }

    @Override
    public List<DictItemDTO> listItems(String dictCode) {
        Dict dict = dictMapper.findByCode(dictCode);
        DomainNotFoundException.assertFound(dict, dictCode);

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initSystemDict(InitDictRequest initDictRequest) {
        DictVO initDict = initDictRequest.getDict();
        Dict dict = Dict.builder()
                .code(initDict.getCode())
                .desc(initDict.getDesc())
                .allowModifyItem(initDict.getAllowModifyItem())
                .build();
        try {
            dictMapper.insert(dict);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("字典重复", e);
        }

        List<DictItemVO> initDictItems = initDictRequest.getDictItems();
        if (CollectionUtils.isNotEmpty(initDictItems)) {
            List<DictItem> dictItems = initDictItems.stream()
                    .map(dictItem -> DictItem.builder()
                            .dictCode(dict.getCode())
                            .key(dictItem.getKey())
                            .value(dictItem.getValue())
                            .sort(dictItem.getSort())
                            .build())
                    .toList();
            try {
                dictItemMapper.insert(dictItems);
            } catch (DuplicateKeyException e) {
                throw new BusinessException("字典项重复", e);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addDictItem(SaveDictItemRequest addDictItemRequest) {
        String dictCode = addDictItemRequest.getDictCode();
        Dict dict = dictMapper.findByCode(dictCode);
        DomainNotFoundException.assertFound(dict, dictCode);

        if (!dict.getAllowModifyItem()) {
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
        Dict dict = dictMapper.findByCode(dictCode);
        DomainNotFoundException.assertFound(dict, dictCode);
        if (!dict.getAllowModifyItem()) {
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
