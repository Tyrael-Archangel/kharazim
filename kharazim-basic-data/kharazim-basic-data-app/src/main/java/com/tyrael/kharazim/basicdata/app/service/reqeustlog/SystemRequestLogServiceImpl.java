package com.tyrael.kharazim.basicdata.app.service.reqeustlog;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.util.CollectionUtils;
import com.tyrael.kharazim.basicdata.app.domain.requestlog.SystemRequestLog;
import com.tyrael.kharazim.basicdata.app.dto.requestlog.NameAndValue;
import com.tyrael.kharazim.basicdata.app.dto.requestlog.PageSystemRequestLogRequest;
import com.tyrael.kharazim.basicdata.app.dto.requestlog.SystemRequestLogConverter;
import com.tyrael.kharazim.basicdata.app.dto.requestlog.SystemRequestLogDTO;
import com.tyrael.kharazim.basicdata.app.mapper.requestlog.SystemRequestLogMapper;
import com.tyrael.kharazim.basicdata.sdk.model.SystemRequestLogVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemRequestLogServiceImpl implements SystemRequestLogService {

    private final SystemRequestLogMapper systemRequestLogMapper;
    private final SystemRequestLogConverter systemRequestLogConverter;

    @Override
    public void save(SystemRequestLogVO logVO) {

        SystemRequestLog log = new SystemRequestLog();
        log.setUri(logVO.getUri());
        log.setRemoteAddr(logVO.getRemoteAddr());
        log.setForwardedFor(logVO.getForwardedFor());
        log.setRealIp(logVO.getRealIp());
        log.setHttpMethod(logVO.getHttpMethod());
        log.setRequestHeaders(this.nameAndValues(logVO.getRequestHeaders()));
        log.setResponseHeaders(this.nameAndValues(logVO.getResponseHeaders()));
        log.setRequestParams(this.nameAndValues(logVO.getRequestParams()));
        log.setResponseStatus(logVO.getResponseStatus());
        log.setRequestBody(logVO.getRequestBody());
        log.setResponseBody(logVO.getResponseBody());
        log.setUserCode(logVO.getUserCode());
        log.setUserName(logVO.getUserName());
        log.setEndpoint(logVO.getEndpoint());
        log.setStartTime(logVO.getStartTime());
        log.setEndTime(logVO.getEndTime());

        systemRequestLogMapper.insert(log);
    }

    private List<NameAndValue> nameAndValues(List<SystemRequestLogVO.NameAndValue> nameAndValues) {
        if (CollectionUtils.isEmpty(nameAndValues)) {
            return new ArrayList<>();
        }
        return nameAndValues.stream()
                .map(e -> new NameAndValue(e.getName(), e.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemRequestLogDTO> latestLogs(Integer rows) {
        List<SystemRequestLog> systemRequestLogs = systemRequestLogMapper.latestRows(rows == null ? 20 : rows);
        return systemRequestLogs.stream()
                .map(systemRequestLogConverter::systemRequestLogDTO)
                .toList();
    }

    @Override
    public PageResponse<SystemRequestLogDTO> page(PageSystemRequestLogRequest pageCommand) {
        PageResponse<SystemRequestLog> pageData = systemRequestLogMapper.page(pageCommand);
        List<SystemRequestLogDTO> systemRequestLogs = pageData.getData()
                .stream()
                .map(systemRequestLogConverter::systemRequestLogDTO)
                .collect(Collectors.toList());
        return PageResponse.success(systemRequestLogs, pageData.getTotalCount());
    }

}
