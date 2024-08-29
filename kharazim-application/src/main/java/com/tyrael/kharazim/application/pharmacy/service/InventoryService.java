package com.tyrael.kharazim.application.pharmacy.service;

import com.tyrael.kharazim.application.pharmacy.vo.inventory.*;
import com.tyrael.kharazim.application.prescription.domain.Prescription;
import com.tyrael.kharazim.common.dto.PageResponse;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/6/3
 */
public interface InventoryService {

    /**
     * 库存分页
     *
     * @param pageRequest {@link PageInventoryRequest}
     * @return 库存分页数据
     */
    PageResponse<InventoryVO> page(PageInventoryRequest pageRequest);

    /**
     * 查询诊所库存数据
     *
     * @param listRequest 诊所+sku
     * @return 诊所库存数据
     */
    List<InventoryVO> listOfClinic(ListInventoryOfClinicRequest listRequest);

    /**
     * 库存日志数据分页
     *
     * @param pageRequest {@link PageInventoryLogRequest}
     * @return 库存日志分页数据
     */
    PageResponse<InventoryLogVO> pageLog(PageInventoryLogRequest pageRequest);

    /**
     * 根据处方预占库存
     *
     * @param prescription {@linkplain Prescription 处方}
     */
    void occupyByPrescription(Prescription prescription);

    /**
     * 入库
     *
     * @param inboundCommand {@link InventoryChangeCommand}
     */
    void inbound(InventoryChangeCommand inboundCommand);

    /**
     * 出库
     *
     * @param outboundCommand {@link InventoryChangeCommand}
     */
    void outbound(InventoryChangeCommand outboundCommand);

}
