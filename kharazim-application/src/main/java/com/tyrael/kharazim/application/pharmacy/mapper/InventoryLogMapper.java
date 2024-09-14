package com.tyrael.kharazim.application.pharmacy.mapper;

import com.tyrael.kharazim.application.base.BasePageMapper;
import com.tyrael.kharazim.application.base.LambdaQueryWrapperX;
import com.tyrael.kharazim.application.pharmacy.domain.InventoryLog;
import com.tyrael.kharazim.application.pharmacy.vo.inventory.PageInventoryLogRequest;
import com.tyrael.kharazim.common.dto.PageResponse;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Tyrael Archangel
 * @since 2024/8/9
 */
@Mapper
public interface InventoryLogMapper extends BasePageMapper<InventoryLog> {

    /**
     * 库存流水日志分页
     *
     * @param pageRequest {@link PageInventoryLogRequest}
     * @return 库存流水日志分页数据
     */
    default PageResponse<InventoryLog> page(PageInventoryLogRequest pageRequest) {
        LambdaQueryWrapperX<InventoryLog> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfHasText(InventoryLog::getSkuCode, pageRequest.getSkuCode());
        queryWrapper.eqIfHasText(InventoryLog::getClinicCode, pageRequest.getClinicCode());
        queryWrapper.eqIfHasText(InventoryLog::getBusinessCode, pageRequest.getBusinessCode());
        queryWrapper.inIfPresent(InventoryLog::getChangeType, pageRequest.getChangeTypes());
        queryWrapper.geIfPresent(InventoryLog::getOperateTime, pageRequest.getStartTime());
        queryWrapper.leIfPresent(InventoryLog::getOperateTime, pageRequest.getEndTime());

        queryWrapper.orderByDesc(InventoryLog::getOperateTime);
        queryWrapper.orderByDesc(InventoryLog::getId);

        return page(pageRequest, queryWrapper);
    }

}
