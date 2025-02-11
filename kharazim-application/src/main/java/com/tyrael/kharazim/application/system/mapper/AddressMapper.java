package com.tyrael.kharazim.application.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.system.domain.Address;
import com.tyrael.kharazim.application.system.enums.AddressLevelEnum;
import com.tyrael.kharazim.common.util.CollectionUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@Mapper
public interface AddressMapper extends BaseMapper<Address> {

    /**
     * list all
     *
     * @return all address
     */
    default List<Address> listAll() {
        LambdaQueryWrapper<Address> queryWrapper = Wrappers.lambdaQuery();
        return selectList(queryWrapper);
    }

    /**
     * 根据节点查询所有子孙节点
     *
     * @param nodeId 节点ID
     * @return addresses
     */
    default List<Address> listByNodeId(Long nodeId) {
        List<Address> result = new ArrayList<>();
        List<Long> tempParentIds = List.of(nodeId);
        while (true) {
            List<Address> addresses = listByParentId(tempParentIds);
            if (CollectionUtils.isEmpty(addresses)) {
                break;
            }

            result.addAll(addresses);
            tempParentIds = addresses.stream()
                    .map(Address::getId)
                    .toList();
        }
        return result;
    }

    /**
     * 根据父节点查询所有子节点
     *
     * @param parentIds 父节点IDs
     * @return addresses
     */
    default List<Address> listByParentId(List<Long> parentIds) {
        if (CollectionUtils.isEmpty(parentIds)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<Address> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(Address::getParentId, parentIds);
        return selectList(queryWrapper);
    }

    /**
     * query by name
     *
     * @param name name
     * @return addresses
     */
    default List<Address> queryByName(String name) {
        LambdaQueryWrapper<Address> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(Address::getName, name);
        return selectList(queryWrapper);
    }

    /**
     * query by code
     *
     * @param code code
     * @return addresses
     */
    default Address queryByCode(String code) {
        LambdaQueryWrapper<Address> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Address::getCode, code);
        return selectOne(queryWrapper);
    }

    /**
     * query by level and parentId
     *
     * @param level    level
     * @param parentId 父节点ID
     * @return addresses
     */
    default List<Address> queryByLevelAndParentId(AddressLevelEnum level, Long parentId) {
        LambdaQueryWrapper<Address> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Address::getLevel, level);
        if (parentId != null) {
            queryWrapper.eq(Address::getParentId, parentId);
        }
        return selectList(queryWrapper);
    }

}
