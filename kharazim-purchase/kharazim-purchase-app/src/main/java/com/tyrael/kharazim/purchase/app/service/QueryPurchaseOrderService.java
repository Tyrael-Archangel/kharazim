package com.tyrael.kharazim.purchase.app.service;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.purchase.app.vo.purchaseorder.PagePurchaseOrderRequest;
import com.tyrael.kharazim.purchase.app.vo.purchaseorder.PurchaseOrderVO;

/**
 * @author Tyrael Archangel
 * @since 2024/5/31
 */
public interface QueryPurchaseOrderService {

    /**
     * 采购单分页查询
     *
     * @param pageRequest {@link PagePurchaseOrderRequest}
     * @return 采购单分页数据
     */
    PageResponse<PurchaseOrderVO> page(PagePurchaseOrderRequest pageRequest);

    /**
     * 采购单详情
     *
     * @param code 采购单号
     * @return 采购单详情
     */
    PurchaseOrderVO detail(String code);

}
