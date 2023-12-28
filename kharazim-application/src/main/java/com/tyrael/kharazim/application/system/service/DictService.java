package com.tyrael.kharazim.application.system.service;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.config.DictCodeConstants;
import com.tyrael.kharazim.application.system.domain.DictItem;
import com.tyrael.kharazim.application.system.dto.dict.*;
import com.tyrael.kharazim.common.dto.MultiResponse;
import com.tyrael.kharazim.common.dto.PageResponse;

import java.util.Collection;
import java.util.List;
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
     * 字典详情
     *
     * @param id 字典ID
     * @return 字典详情
     */
    DictDTO dictDetail(Long id);

    /**
     * 新增字典
     *
     * @param addDictRequest SaveDictRequest
     * @param currentUser    操作人
     */
    void addDict(SaveDictRequest addDictRequest, AuthUser currentUser);

    /**
     * 修改字典
     *
     * @param dictId            字典ID
     * @param modifyDictRequest SaveDictRequest
     * @param currentUser       操作人
     */
    void modify(Long dictId, SaveDictRequest modifyDictRequest, AuthUser currentUser);

    /**
     * 删除字典
     *
     * @param ids 字典ID
     */
    void delete(List<Long> ids);

    /**
     * 字典项列表
     *
     * @param dictCode 字典编码
     * @return 字典项列表数据
     */
    MultiResponse<DictItemOptionDTO> listDictItem(String dictCode);

    /**
     * find items by dictCode
     *
     * @param dictCode dictCode
     * @return DictItems
     */
    List<DictItem> findByDictCode(String dictCode);

    /**
     * find items by dictCode
     *
     * @param dictCodeConstant {@link DictCodeConstants}
     * @return DictItems
     */
    List<DictItem> findByDict(DictCodeConstants dictCodeConstant);

    /**
     * 字典项 value -> name
     *
     * @param dictCodeConstant {@link DictCodeConstants}
     * @return 字典项 value -> name
     */
    Map<String, String> dictItemMap(DictCodeConstants dictCodeConstant);

    /**
     * 查询字典项名称
     *
     * @param dictCode      字典
     * @param dictItemValue 字典项值
     * @return 字典项名称
     */
    String findItemName(String dictCode, String dictItemValue);

    /**
     * 查询字典项
     *
     * @param dictCode      字典
     * @param dictItemValue 字典项值
     * @return 字典项
     */
    DictItem findItem(String dictCode, String dictItemValue);

    /**
     * 查询字典项名称
     *
     * @param dictCodeConstants {@link DictCodeConstants}
     * @param dictItemValue     字典项值
     * @return 字典项名称
     */
    String findItemName(DictCodeConstants dictCodeConstants, String dictItemValue);

    /**
     * list dict item values by dictCode
     *
     * @param dictCode dictCode
     * @return dict item codes
     */
    Set<String> findEnabledItems(String dictCode);

    /**
     * list dict item values by dictCodeConstants
     *
     * @param dictCodeConstants {@link DictCodeConstants}
     * @return dict item codes
     */
    Set<String> findEnabledItems(DictCodeConstants dictCodeConstants);

    /**
     * 验证字典值是否有效
     *
     * @param dictCode       字典编码
     * @param dictItemValues 字典值
     */
    void ensureDictItemEnable(String dictCode, Collection<String> dictItemValues);

    /**
     * 验证字典值是否有效
     *
     * @param dictCode      字典编码
     * @param dictItemValue 字典值
     */
    void ensureDictItemEnable(String dictCode, String dictItemValue);

    /**
     * 验证字典值是否有效
     *
     * @param dictCodeConstants {@link DictCodeConstants}
     * @param dictItemValues    字典值
     */
    void ensureDictItemEnable(DictCodeConstants dictCodeConstants, Collection<String> dictItemValues);

    /**
     * 验证字典值是否有效
     *
     * @param dictCodeConstants {@link DictCodeConstants}
     * @param dictItemValue     字典值
     */
    void ensureDictItemEnable(DictCodeConstants dictCodeConstants, String dictItemValue);

    /**
     * 字典项分页
     *
     * @param pageDictItemRequest PageDictItemRequest
     * @return 字典项分页数据
     */
    PageResponse<DictItemDTO> pageDictItem(PageDictItemRequest pageDictItemRequest);

    /**
     * 字典项详情
     *
     * @param dictItemId 字典项ID
     * @return 字典项详情
     */
    DictItemDTO dictItemDetail(Long dictItemId);

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

    /**
     * 删除字典项
     *
     * @param dictItemIds 字典项ID
     * @param currentUser 操作人
     */
    void deleteItem(List<Long> dictItemIds, AuthUser currentUser);

}
