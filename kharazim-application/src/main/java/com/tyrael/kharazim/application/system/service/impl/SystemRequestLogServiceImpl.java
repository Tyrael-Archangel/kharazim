package com.tyrael.kharazim.application.system.service.impl;

import com.tyrael.kharazim.application.system.domain.SystemRequestLog;
import com.tyrael.kharazim.application.system.dto.requestlog.SystemRequestLogDTO;
import com.tyrael.kharazim.application.system.mapper.SystemRequestLogMapper;
import com.tyrael.kharazim.application.system.service.SystemRequestLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemRequestLogServiceImpl implements SystemRequestLogService {

    private final SystemRequestLogMapper systemRequestLogMapper;

    @Override
    public void save(SystemRequestLog systemRequestLog) {
        systemRequestLogMapper.insert(systemRequestLog);
    }

    @Override
    public List<SystemRequestLogDTO> latestLogs(Integer rows) {

        List<SystemRequestLog> systemRequestLogs = systemRequestLogMapper.latestRows(rows == null ? 20 : rows);

        return systemRequestLogs.stream()
                .map(e -> SystemRequestLogDTO.builder()
                        .id(e.getId())
                        .uri(e.getUri())
                        .remoteAddr(e.getRemoteAddr())
                        .forwardedFor(e.getForwardedFor())
                        .realIp(e.getRealIp())
                        .requestHeaders(e.getRequestHeaders())
                        .responseHeaders(e.getResponseHeaders())
                        .requestParams(e.getRequestParams())
                        .responseStatus(e.getResponseStatus())
                        .requestBody(e.getRequestBody())
                        .responseBody(e.getResponseBody())
                        .userName(e.getUserName())
                        .startTime(e.getStartTime())
                        .endTime(e.getEndTime())
                        .costMills(e.getCostMills())
                        .build())
                .toList();
    }

}
