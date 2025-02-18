package com.tyrael.kharazim.lib.web.config;

import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.base.dto.Response;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tyrael Archangel
 * @since 2024/1/5
 */
@RestController
public class SystemErrorController implements ErrorController {

    @RequestMapping("${server.error.path:${error.path:/error}}")
    public Response error(HttpServletRequest request) {
        HttpStatus httpStatus = getStatus(request);
        Object errorUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        return DataResponse.fail(errorUri, httpStatus.value(), httpStatus.getReasonPhrase());
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}
