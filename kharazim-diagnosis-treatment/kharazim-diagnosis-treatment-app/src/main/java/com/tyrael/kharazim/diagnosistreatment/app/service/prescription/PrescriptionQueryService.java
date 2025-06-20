package com.tyrael.kharazim.diagnosistreatment.app.service.prescription;

import com.tyrael.kharazim.base.dto.PageCommand;
import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.diagnosistreatment.app.vo.prescription.DailySalesModels;
import com.tyrael.kharazim.diagnosistreatment.app.vo.prescription.PagePrescriptionRequest;
import com.tyrael.kharazim.diagnosistreatment.app.vo.prescription.PrescriptionVO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/3/14
 */
public interface PrescriptionQueryService {

    /**
     * 处方详情
     *
     * @param code 处方编码
     * @return 处方详情
     */
    PrescriptionVO detail(String code);

    /**
     * 分页查询
     *
     * @param pageRequest {@link PagePrescriptionRequest}
     * @return 处方分页数据
     */
    PageResponse<PrescriptionVO> page(PagePrescriptionRequest pageRequest);

    /**
     * export all by cursor
     *
     * @param httpServletResponse HttpServletResponse
     * @throws IOException IOException
     */
    void exportAll(HttpServletResponse httpServletResponse) throws IOException;

    /**
     * 处方导出
     *
     * @param pageRequest         {@link PagePrescriptionRequest}
     * @param httpServletResponse HttpServletResponse
     * @throws IOException IOException
     */
    void export(PagePrescriptionRequest pageRequest, HttpServletResponse httpServletResponse) throws IOException;

    /**
     * 每日销售数据统计趋势
     *
     * @param filter 过滤条件
     * @return 每日销售数据统计
     */
    List<DailySalesModels.View> dailySalesTrends(DailySalesModels.FilterCommand filter);

    /**
     * 每日销售数据统计分页查询
     *
     * @param filter      过滤条件
     * @param pageCommand 分页条件
     * @return 每日销售数据统计分页数据
     */
    PageResponse<DailySalesModels.View> dailySalesPage(DailySalesModels.FilterCommand filter, PageCommand pageCommand);

    /**
     * 每日销售数据统计导出
     *
     * @param filter   过滤条件
     * @param response HttpServletResponse
     * @throws IOException IOException
     */
    void dailySalesExport(DailySalesModels.FilterCommand filter, HttpServletResponse response) throws IOException;

}
