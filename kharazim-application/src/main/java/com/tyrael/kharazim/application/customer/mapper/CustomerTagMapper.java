package com.tyrael.kharazim.application.customer.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.application.customer.domain.CustomerTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/1/18
 */
@Mapper
public interface CustomerTagMapper extends BaseMapper<CustomerTag> {

    /**
     * list by
     *
     * @param customerCode 会员编码
     * @return CustomerTags
     */
    default List<CustomerTag> listByCustomerCode(String customerCode) {
        LambdaQueryWrapper<CustomerTag> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CustomerTag::getCustomerCode, customerCode);
        queryWrapper.eq(CustomerTag::getDeletedTimestamp, 0);
        queryWrapper.orderByAsc(CustomerTag::getId);
        return selectList(queryWrapper);
    }

    /**
     * 删除会员标签
     *
     * @param customerCode 会员编码
     * @param tagDictKey   会员标签字典键
     */
    default void deleteCustomerTag(String customerCode, String tagDictKey) {
        LambdaUpdateWrapper<CustomerTag> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(CustomerTag::getCustomerCode, customerCode)
                .eq(CustomerTag::getTagDict, tagDictKey)
                .set(CustomerTag::getDeleted, Boolean.TRUE)
                .set(CustomerTag::getDeletedTimestamp, System.currentTimeMillis());
        this.update(null, updateWrapper);
    }

}
