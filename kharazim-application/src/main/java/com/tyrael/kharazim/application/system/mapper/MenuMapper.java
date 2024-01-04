package com.tyrael.kharazim.application.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.system.domain.Menu;
import com.tyrael.kharazim.application.system.dto.menu.QueryMenuRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * all menus
     *
     * @return all menus
     */
    default List<Menu> listAll() {
        LambdaQueryWrapper<Menu> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByAsc(Menu::getSort);
        return selectList(queryWrapper);
    }

    /**
     * list by condition
     *
     * @param queryMenuRequest QueryMenuRequest
     * @return menus
     */
    default List<Menu> list(QueryMenuRequest queryMenuRequest) {
        LambdaQueryWrapper<Menu> queryWrapper = Wrappers.lambdaQuery();
        String keywords = queryMenuRequest.getKeywords();
        if (StringUtils.isNotBlank(keywords)) {
            queryWrapper.like(Menu::getName, keywords);
        }
        return selectList(queryWrapper);
    }

}
