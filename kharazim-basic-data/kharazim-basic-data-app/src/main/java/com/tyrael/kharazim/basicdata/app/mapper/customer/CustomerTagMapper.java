package com.tyrael.kharazim.basicdata.app.mapper.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tyrael.kharazim.basicdata.app.domain.customer.CustomerTag;
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
        LambdaQueryWrapper<CustomerTag> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CustomerTag::getCustomerCode, customerCode)
                .eq(CustomerTag::getTagDict, tagDictKey);
        queryWrapper.orderByAsc(CustomerTag::getId);
        this.delete(queryWrapper);
    }

}
