package com.tyrael.kharazim.application.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.system.domain.Menu;
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

}
