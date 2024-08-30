package com.tyrael.kharazim.application.pharmacy.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tyrael.kharazim.application.base.BasePageMapper;
import com.tyrael.kharazim.application.pharmacy.domain.InventoryOccupy;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.PageInventoryOccupyRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author Tyrael Archangel
 * @since 2024/8/30
 */
@Mapper
public interface InventoryOccupyMapper extends BasePageMapper<InventoryOccupy> {

    /**
     * page
     *
     * @param pageRequest {@link PageInventoryOccupyRequest}
     * @return 分页数据
     */
    default PageResponse<InventoryOccupy> page(PageInventoryOccupyRequest pageRequest) {
        LambdaQueryWrapper<InventoryOccupy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InventoryOccupy::getSkuCode, pageRequest.getSkuCode())
                .eq(InventoryOccupy::getClinicCode, pageRequest.getClinicCode())
                .ne(InventoryOccupy::getQuantity, 0);

        queryWrapper.orderByDesc(InventoryOccupy::getQuantity);
        queryWrapper.orderByDesc(InventoryOccupy::getId);

        return selectPage(pageRequest, queryWrapper);
    }

    /**
     * 增加预占
     *
     * @param clinicCode   诊所编码
     * @param skuCode      SKU编码
     * @param businessCode 业务编码
     * @param quantity     数量
     */
    @Update("""
            insert into inventory_occupy
                (business_code, clinic_code, sku_code, quantity)
            values
                (#{businessCode}, #{clinicCode}, #{skuCode}, #{quantity})
            on duplicate key update
                quantity = quantity + values(quantity)
            """)
    void occupy(@Param("clinicCode") String clinicCode,
                @Param("skuCode") String skuCode,
                @Param("businessCode") String businessCode,
                @Param("quantity") int quantity);

    /**
     * 释放预占
     *
     * @param clinicCode   诊所编码
     * @param skuCode      SKU编码
     * @param businessCode 业务编码
     * @param quantity     数量
     */
    default void release(String clinicCode, String skuCode, String businessCode, int quantity) {
        occupy(clinicCode, skuCode, businessCode, -quantity);
    }

}
