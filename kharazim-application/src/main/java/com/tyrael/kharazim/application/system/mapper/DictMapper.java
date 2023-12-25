package com.tyrael.kharazim.application.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Sets;
import com.tyrael.kharazim.application.system.domain.Dict;
import com.tyrael.kharazim.application.system.dto.dict.PageDictRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Mapper
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * list by codes
     *
     * @param codes codes
     * @return Dicts
     */
    default List<Dict> listByCodes(Collection<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }
        Set<String> distinctCodes = Sets.newHashSet(codes);
        distinctCodes.removeIf(e -> !StringUtils.hasText(e));

        if (distinctCodes.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Dict> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(Dict::getCode, distinctCodes);
        return selectList(queryWrapper);
    }

    /**
     * find by code
     *
     * @param code 字典编码
     * @return Dict
     */
    default Dict findByCode(String code) {
        if (!StringUtils.hasText(code)) {
            return null;
        }
        LambdaQueryWrapper<Dict> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Dict::getCode, code);
        return selectOne(queryWrapper);
    }

    /**
     * page
     *
     * @param pageDictRequest PageDictRequest
     * @return dictPageResponse
     */
    default PageResponse<Dict> page(PageDictRequest pageDictRequest) {
        LambdaQueryWrapper<Dict> queryWrapper = Wrappers.lambdaQuery();
        String keywords = pageDictRequest.getKeywords();
        if (StringUtils.hasText(keywords)) {
            queryWrapper.like(Dict::getCode, keywords)
                    .or()
                    .like(Dict::getName, keywords);
        }
        Page<Dict> page = new Page<>(pageDictRequest.getPageNum(), pageDictRequest.getPageSize());
        Page<Dict> dictPage = selectPage(page, queryWrapper);

        return PageResponse.success(dictPage.getRecords(),
                dictPage.getTotal(),
                pageDictRequest.getPageSize(),
                pageDictRequest.getPageNum());
    }

    /**
     * batchUpdateUpdater
     *
     * @param dictCodes   字典项
     * @param updater     updater
     * @param updaterCode updaterCode
     * @param updateTime  updateTime
     */
    default void batchUpdateUpdater(Set<String> dictCodes, String updater, String updaterCode, LocalDateTime updateTime) {
        LambdaUpdateWrapper<Dict> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.in(Dict::getCode, dictCodes);
        updateWrapper.set(Dict::getUpdater, updater)
                .set(Dict::getUpdaterCode, updaterCode)
                .set(Dict::getUpdateTime, updateTime);
        this.update(null, updateWrapper);
    }

}
