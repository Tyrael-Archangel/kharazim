package com.tyrael.kharazim.basicdata.app.dto.requestlog;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统日志")
public class SystemRequestLogDTO {

    @Schema(description = "日志ID")
    private Long id;

    @Schema(description = "请求URI")
    private String uri;

    @Schema(description = "来源地址")
    private String remoteAddr;

    @Schema(description = "nginx代理添加的X-Forwarded-For")
    private String forwardedFor;

    @Schema(description = "nginx代理添加的X-Real-IP")
    private String realIp;

    @Schema(description = "请求头")
    private List<NameAndValue> requestHeaders;

    @Schema(description = "响应头")
    private List<NameAndValue> responseHeaders;

    @Schema(description = "请求参数")
    private List<NameAndValue> requestParams;

    @Schema(description = "状态码")
    private Integer responseStatus;

    @Schema(description = "请求数据")
    private String requestBody;

    @Schema(description = "返回数据")
    private String responseBody;

    @Schema(description = "请求用户")
    private String userName;

    @Schema(description = "endpoint")
    private String endpoint;

    @Schema(description = "请求发起时间", format = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime startTime;

    @Schema(description = "请求结束时间", format = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime endTime;

    @Schema(description = "耗时（毫秒）")
    private Long costMills;

}
