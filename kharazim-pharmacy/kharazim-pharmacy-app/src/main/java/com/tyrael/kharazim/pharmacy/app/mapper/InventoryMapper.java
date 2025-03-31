package com.tyrael.kharazim.pharmacy.app.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.mybatis.BasePageMapper;
import com.tyrael.kharazim.mybatis.LambdaQueryWrapperX;
import com.tyrael.kharazim.pharmacy.app.domain.Inventory;
import com.tyrael.kharazim.pharmacy.app.vo.inventory.ListInventoryOfClinicRequest;
import com.tyrael.kharazim.pharmacy.app.vo.inventory.PageInventoryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Mapper
public interface InventoryMapper extends BasePageMapper<Inventory> {

    /**
     * find by unique key
     *
     * @param clinicCode 诊所编码
     * @param skuCode    SKU编码
     * @return Inventory
     */
    default Inventory findOne(String clinicCode, String skuCode) {
        LambdaUpdateWrapper<Inventory> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(Inventory::getSkuCode, skuCode);
        queryWrapper.eq(Inventory::getClinicCode, clinicCode);
        return selectOne(queryWrapper);
    }

    /**
     * 库存分页
     *
     * @param pageRequest {@link PageInventoryRequest}
     * @return 库存分页数据
     */
    default PageResponse<Inventory> page(PageInventoryRequest pageRequest) {
        LambdaQueryWrapperX<Inventory> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.inIfPresent(Inventory::getClinicCode, pageRequest.getClinicCodes());
        queryWrapper.eqIfHasText(Inventory::getSkuCode, pageRequest.getSkuCode());
        queryWrapper.inIfPresent(Inventory::getSkuCode, pageRequest.getFilterSkuCodes());

        PageInventoryRequest.SortBy sortBy = pageRequest.getSortByOrDefault();
        boolean isAsc = pageRequest.isAsc();

        switch (sortBy) {
            case QUANTITY -> queryWrapper.orderBy(isAsc, Inventory::getQuantity);
            case OCCUPIED_QUANTITY -> queryWrapper.orderBy(isAsc, Inventory::getOccupiedQuantity);
            case USABLE_QUANTITY -> queryWrapper.orderBy(isAsc, Inventory::getUsableQuantity);
        }
        queryWrapper.orderBy(isAsc, Inventory::getId);

        return page(pageRequest, queryWrapper);
    }

    /**
     * 查询诊所库存数据
     *
     * @param listRequest 诊所+sku
     * @return 诊所库存数据
     */
    default List<Inventory> listOfClinic(ListInventoryOfClinicRequest listRequest) {
        LambdaQueryWrapperX<Inventory> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eq(Inventory::getClinicCode, listRequest.getClinicCode());
        queryWrapper.inIfPresent(Inventory::getSkuCode, listRequest.getSkuCodes());
        return selectList(queryWrapper);
    }

    /**
     * 增加库存
     *
     * @param skuCode    SKU编码
     * @param clinicCode 诊所编码
     * @param quantity   数量
     * @return updated rows
     */
    @Update("""
            update `inventory`
            set `quantity` = `quantity` + #{quantity}
            where `clinic_code` = #{clinicCode}
              and `sku_code` = #{skuCode}
            """)
    int increaseQuantity(@Param("clinicCode") String clinicCode,
                         @Param("skuCode") String skuCode,
                         @Param("quantity") Integer quantity);

    /**
     * 预占库存
     *
     * @param clinicCode     诊所编码
     * @param skuCode        SKU编码
     * @param occupyQuantity 预占数量
     * @return 是否预占成功
     */
    @Update("""
            update `inventory`
            set `occupied_quantity` = `occupied_quantity` + #{occupyQuantity}
            where `clinic_code` = #{clinicCode}
             and `sku_code` = #{skuCode}
             and (`quantity` - `occupied_quantity`) >= #{occupyQuantity}
            """)
    int increaseOccupy(@Param("clinicCode") String clinicCode,
                       @Param("skuCode") String skuCode,
                       @Param("occupyQuantity") int occupyQuantity);

    /**
     * 根据预占减少库存
     *
     * @param clinicCode 诊所编码
     * @param skuCode    SKU编码
     * @param quantity   数量
     */
    @Update("""
            update `inventory`
             set `quantity` = `quantity` - #{quantity},
                 `occupied_quantity` = `occupied_quantity` - #{quantity}
            where `clinic_code` = #{clinicCode}
             and `sku_code` = #{skuCode}
            """)
    void decreaseQuantityByOccupy(@Param("clinicCode") String clinicCode,
                                  @Param("skuCode") String skuCode,
                                  @Param("quantity") Integer quantity);

}
