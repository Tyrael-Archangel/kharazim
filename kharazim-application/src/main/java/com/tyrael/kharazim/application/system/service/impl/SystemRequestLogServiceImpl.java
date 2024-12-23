package com.tyrael.kharazim.application.system.service.impl;

import com.tyrael.kharazim.application.system.domain.SystemRequestLog;
import com.tyrael.kharazim.application.system.dto.requestlog.PageSystemRequestLogRequest;
import com.tyrael.kharazim.application.system.dto.requestlog.SystemEndpointDTO;
import com.tyrael.kharazim.application.system.dto.requestlog.SystemRequestLogConverter;
import com.tyrael.kharazim.application.system.dto.requestlog.SystemRequestLogDTO;
import com.tyrael.kharazim.application.system.mapper.SystemRequestLogMapper;
import com.tyrael.kharazim.application.system.service.SystemRequestLogService;
import com.tyrael.kharazim.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemRequestLogServiceImpl implements SystemRequestLogService {

    private static final String DISABLE_SYSTEM_LOG_ENDPOINTS = "SYSTEM_LOG_DISABLED_ENDPOINTS";

    private final SystemRequestLogMapper systemRequestLogMapper;
    private final StringRedisTemplate redisTemplate;
    private final BeanFactory beanFactory;
    private final SystemRequestLogConverter systemRequestLogConverter;

    @Override
    public void save(SystemRequestLog systemRequestLog) {
        systemRequestLogMapper.insert(systemRequestLog);
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

    @Override
    public List<SystemEndpointDTO> endpoints() {
        RequestMappingHandlerMapping requestMappingHandlerMapping = beanFactory.getBean(
                "requestMappingHandlerMapping", RequestMappingHandlerMapping.class);

        Set<String> disabledEndpoints = new HashSet<>(disabledEndpoints());
        Set<String> allHistoryEndpoints = systemRequestLogMapper.allHistoryEndpoints();
        Set<String> allActiveEndpoints = requestMappingHandlerMapping.getHandlerMethods()
                .keySet()
                .stream()
                .map(systemRequestLogConverter::requestMappingInfoEndpoint)
                .collect(Collectors.toSet());

        return Stream.concat(allHistoryEndpoints.stream(), allActiveEndpoints.stream())
                .distinct()
                .map(e -> {
                    SystemEndpointDTO endpointDTO = new SystemEndpointDTO();
                    endpointDTO.setEndpoint(e);
                    endpointDTO.setActive(allActiveEndpoints.contains(e));
                    endpointDTO.setEnableSystemLog(!disabledEndpoints.contains(e));
                    return endpointDTO;
                })
                .sorted(Comparator.comparing(SystemEndpointDTO::getEndpoint))
                .toList();
    }

    private Set<String> disabledEndpoints() {
        try (Cursor<String> cursor = redisTemplate.opsForSet()
                .scan(DISABLE_SYSTEM_LOG_ENDPOINTS, ScanOptions.NONE)) {
            return cursor.stream().collect(Collectors.toSet());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableEndpointLog(String endpoint) {
        redisTemplate.opsForSet()
                .add(DISABLE_SYSTEM_LOG_ENDPOINTS, endpoint);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableEndpointLog(String endpoint) {
        redisTemplate.opsForSet()
                .remove(DISABLE_SYSTEM_LOG_ENDPOINTS, endpoint);
    }

    @Override
    public boolean isEndpointLogEnabled(String endpoint) {
        Boolean contains = redisTemplate.opsForSet()
                .isMember(DISABLE_SYSTEM_LOG_ENDPOINTS, endpoint);
        return !Boolean.TRUE.equals(contains);
    }

}
