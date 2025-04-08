package com.tyrael.kharazim.pharmacy.app.service;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.pharmacy.app.vo.inventory.*;

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
    PageResponse<InventoryDTO> page(PageInventoryRequest pageRequest);

    /**
     * 查询诊所库存数据
     *
     * @param listRequest 诊所+sku
     * @return 诊所库存数据
     */
    List<InventoryDTO> listOfClinic(ListInventoryOfClinicRequest listRequest);

    /**
     * 库存日志数据分页
     *
     * @param pageRequest {@link PageInventoryLogRequest}
     * @return 库存日志分页数据
     */
    PageResponse<InventoryLogVO> pageLog(PageInventoryLogRequest pageRequest);

    /**
     * 库存预占数据分页
     *
     * @param pageRequest {@link PageInventoryOccupyRequest}
     * @return 库存预占分页数据
     */
    PageResponse<InventoryOccupyVO> pageOccupy(PageInventoryOccupyRequest pageRequest);

    /**
     * 预占库存
     *
     * @param occupyCommand {@link InventoryOccupyCommand}
     */
    void occupy(InventoryOccupyCommand occupyCommand);

    /**
     * 入库
     *
     * @param inboundCommand {@link InventoryInboundCommand}
     */
    void inbound(InventoryInboundCommand inboundCommand);

    /**
     * 出库
     *
     * @param outboundCommand {@link InventoryOutboundCommand}
     */
    void outbound(InventoryOutboundCommand outboundCommand);

}
