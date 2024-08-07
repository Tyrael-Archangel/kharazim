package com.tyrael.kharazim.application.pharmacy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.pharmacy.domain.Inventory;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.PageInventoryRequest;
import com.tyrael.kharazim.application.product.mapper.ProductSkuMapper;
import com.tyrael.kharazim.common.dto.PageResponse;
import com.tyrael.kharazim.common.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {

    /**
     * 增加库存
     *
     * @param skuCode    SKU编码
     * @param clinicCode 诊所编码
     * @param quantity   数量
     */
    @Update("update `inventory` set `quantity` = `quantity` + #{quantity} " +
            "where `clinic_code` = #{clinicCode} " +
            "  and `sku_code` = #{skuCode}")
    void increaseQuantity(@Param("skuCode") String skuCode,
                          @Param("clinicCode") String clinicCode,
                          @Param("quantity") Integer quantity);

    /**
     * 预占库存
     *
     * @param clinicCode     诊所编码
     * @param skuCode        SKU编码
     * @param occupyQuantity 预占数量
     * @return 是否预占成功
     */
    @Update("update `inventory` " +
            "set `occupied_quantity` = `occupied_quantity` + #{occupyQuantity} " +
            "where `clinic_code` = #{clinicCode} " +
            "  and `sku_code` = #{skuCode} " +
            "  and (`quantity` - `occupied_quantity`) >= #{occupyQuantity}")
    int increaseOccupy(@Param("clinicCode") String clinicCode,
                       @Param("skuCode") String skuCode,
                       @Param("occupyQuantity") int occupyQuantity);

    /**
     * 库存分页
     *
     * @param pageRequest {@link PageInventoryRequest}
     * @return 库存分页数据
     */
    default PageResponse<Inventory> page(PageInventoryRequest pageRequest) {
        LambdaQueryWrapperX<Inventory> queryWrapper = new LambdaQueryWrapperX<>();
        String skuName = pageRequest.getSkuName();
        if (StringUtils.isNotBlank(skuName)) {
            List<String> skuCodes = ProductSkuMapper.filterSkuCodesByName(skuName);
            if (CollectionUtils.isEmpty(skuCodes)) {
                return PageResponse.success(new ArrayList<>(),
                        0L,
                        pageRequest.getPageSize(),
                        pageRequest.getPageNum());
            } else {
                queryWrapper.in(Inventory::getSkuCode, skuCodes);
            }
        }
        queryWrapper.inIfPresent(Inventory::getClinicCode, pageRequest.getClinicCodes());
        queryWrapper.eqIfHasText(Inventory::getSkuCode, pageRequest.getSkuCode());

        Page<Inventory> pageCondition = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<Inventory> pageData = selectPage(pageCondition, queryWrapper);
        return PageResponse.success(
                pageData.getRecords(),
                pageData.getTotal(),
                (int) pageCondition.getSize(),
                (int) pageCondition.getCurrent());
    }

}
