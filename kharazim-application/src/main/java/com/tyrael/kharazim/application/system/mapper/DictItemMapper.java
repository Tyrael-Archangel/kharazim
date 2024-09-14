package com.tyrael.kharazim.application.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.base.BasePageMapper;
import com.tyrael.kharazim.application.system.domain.DictItem;
import com.tyrael.kharazim.application.system.dto.dict.PageDictItemRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collections;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Mapper
public interface DictItemMapper extends BasePageMapper<DictItem> {

    /**
     * update dictCode
     *
     * @param oldDictCode 旧的字典编码
     * @param newDictCode 新的字典编码
     */
    default void updateDictCode(String oldDictCode, String newDictCode) {
        if (StringUtils.equals(oldDictCode, newDictCode)) {
            return;
        }

        LambdaUpdateWrapper<DictItem> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DictItem::getDictCode, newDictCode);
        updateWrapper.set(DictItem::getDictCode, oldDictCode);

        this.update(null, updateWrapper);
    }

    /**
     * delete by dictCodes
     *
     * @param dictCodes 字典编码
     */
    default void deleteByDictCodes(List<String> dictCodes) {
        if (CollectionUtils.isEmpty(dictCodes)) {
            return;
        }
        LambdaQueryWrapper<DictItem> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(DictItem::getDictCode, dictCodes);
        this.delete(queryWrapper);
    }

    /**
     * find by dictCode
     *
     * @param dictCode 字典编码
     * @return entities
     */
    default List<DictItem> findByDictCode(String dictCode) {
        if (StringUtils.isBlank(dictCode)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<DictItem> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DictItem::getDictCode, dictCode);
        return this.selectList(queryWrapper);
    }

    /**
     * find enabled dictItems by dictCode
     *
     * @param dictCode 字典编码
     * @return DictItems
     */
    default List<DictItem> findEnabledByDictCode(String dictCode) {
        if (StringUtils.isBlank(dictCode)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<DictItem> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DictItem::getDictCode, dictCode)
                .eq(DictItem::getEnabled, Boolean.TRUE);
        return this.selectList(queryWrapper);
    }

    /**
     * page
     *
     * @param pageRequest PageDictItemRequest
     * @return entities
     */
    default PageResponse<DictItem> page(PageDictItemRequest pageRequest) {
        LambdaQueryWrapper<DictItem> queryWrapper = Wrappers.lambdaQuery();
        String dictCode = pageRequest.getTypeCode();
        if (StringUtils.isNotBlank(dictCode)) {
            queryWrapper.eq(DictItem::getDictCode, dictCode);
        }

        String keywords = pageRequest.getKeywords();
        if (StringUtils.isNotBlank(keywords)) {
            queryWrapper.and(q -> q.like(DictItem::getName, keywords)
                    .or()
                    .like(DictItem::getValue, keywords));
        }

        queryWrapper.orderByAsc(DictItem::getSort)
                .orderByAsc(DictItem::getId);

        return page(pageRequest, queryWrapper);
    }

}
