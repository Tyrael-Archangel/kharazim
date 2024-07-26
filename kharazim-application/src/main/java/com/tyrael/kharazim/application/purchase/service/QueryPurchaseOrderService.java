package com.tyrael.kharazim.application.purchase.service;

import com.tyrael.kharazim.application.purchase.vo.PurchaseOrderVO;
import com.tyrael.kharazim.application.purchase.vo.request.PagePurchaseOrderRequest;
import com.tyrael.kharazim.common.dto.PageResponse;

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
