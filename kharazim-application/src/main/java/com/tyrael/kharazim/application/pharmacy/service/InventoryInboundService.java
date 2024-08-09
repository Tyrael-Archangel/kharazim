package com.tyrael.kharazim.application.pharmacy.service;

import com.tyrael.kharazim.application.pharmacy.vo.inventory.InventoryChangeCommand;

/**
 * @author Tyrael Archangel
 * @since 2024/8/9
 */
public interface InventoryInboundService {

    /**
     * 入库
     *
     * @param inboundCommand {@link InventoryChangeCommand}
     */
    void inbound(InventoryChangeCommand inboundCommand);

}
