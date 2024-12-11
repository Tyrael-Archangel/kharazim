package com.tyrael.kharazim.application.system.service;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.system.domain.DictConstant;
import com.tyrael.kharazim.application.system.dto.dict.DictDTO;
import com.tyrael.kharazim.application.system.dto.dict.DictItemDTO;
import com.tyrael.kharazim.application.system.dto.dict.PageDictRequest;
import com.tyrael.kharazim.application.system.dto.dict.SaveDictItemRequest;
import com.tyrael.kharazim.common.dto.MultiResponse;
import com.tyrael.kharazim.common.dto.PageResponse;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
public interface DictService {

    /**
     * 字典分页查询
     *
     * @param pageDictRequest PageDictRequest
     * @return 字典分页数据
     */
    PageResponse<DictDTO> pageDict(PageDictRequest pageDictRequest);

    /**
     * 字典项列表
     *
     * @param dictCode 字典编码
     * @return 字典项列表数据
     */
    MultiResponse<DictItemDTO> listItems(String dictCode);

    /**
     * 字典项 key -> value
     *
     * @param dictConstant {@link DictConstant}
     * @return 字典项 key -> value
     */
    Map<String, String> dictItemMap(DictConstant dictConstant);

    /**
     * 查询字典项值
     *
     * @param dictConstant {@link DictConstant}
     * @param dictItemKey  字典项键
     * @return 字典项名称
     */
    String findItemValue(DictConstant dictConstant, String dictItemKey);

    /**
     * list dict item values by dictCodeConstants
     *
     * @param dictConstant {@link DictConstant}
     * @return dict item codes
     */
    default Set<String> dictItemKeys(DictConstant dictConstant) {
        return Set.copyOf(dictItemMap(dictConstant).values());
    }

    /**
     * 验证字典值是否有效
     *
     * @param dictConstant {@link DictConstant}
     * @param dictItemKeys 字典值
     */
    void ensureDictItemEnable(DictConstant dictConstant, Collection<String> dictItemKeys);

    /**
     * 验证字典值是否有效
     *
     * @param dictConstant {@link DictConstant}
     * @param dictItemKey  字典键
     */
    void ensureDictItemEnable(DictConstant dictConstant, String dictItemKey);

    /**
     * 添加字典项
     *
     * @param addDictItemRequest SaveDictItemRequest
     * @param currentUser        操作人
     */
    void addDictItem(SaveDictItemRequest addDictItemRequest, AuthUser currentUser);

    /**
     * 修改字典项
     *
     * @param dictItemId            字典项ID
     * @param modifyDictItemRequest SaveDictItemRequest
     * @param currentUser           操作人
     */
    void modifyDictItem(Long dictItemId, SaveDictItemRequest modifyDictItemRequest, AuthUser currentUser);

}
