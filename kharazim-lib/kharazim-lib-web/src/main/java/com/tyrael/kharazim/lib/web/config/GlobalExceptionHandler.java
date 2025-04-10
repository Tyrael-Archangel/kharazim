package com.tyrael.kharazim.lib.web.config;

import com.tyrael.kharazim.base.dto.ExceptionResponse;
import com.tyrael.kharazim.base.dto.Response;
import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.base.exception.ForbiddenException;
import com.tyrael.kharazim.base.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * @author Tyrael Archangel
 * @since 2023/12/22
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final SystemGlobalWebConfig systemGlobalWebConfig;

    private boolean enablePrintException() {
        return systemGlobalWebConfig.isEnablePrintExceptionStackTrace();
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleException(Exception e, HttpServletRequest httpServletRequest) {
        log.error("{} {} {}", httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), e.getMessage(), e);
        return new ExceptionResponse(e, HttpStatus.INTERNAL_SERVER_ERROR.value(), enablePrintException());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleException(NoResourceFoundException e, HttpServletRequest httpServletRequest) {
        log.warn("{} {} {}", httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), e.getMessage());
        return new ExceptionResponse(e, HttpStatus.NOT_FOUND.value(), enablePrintException());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ExceptionResponse handleHttpMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest httpServletRequest) {
        log.error("{} {} {}", httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), e.getMessage());
        return new ExceptionResponse(e, HttpStatus.METHOD_NOT_ALLOWED.value(), enablePrintException());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()) {
            ObjectError objectError = bindingResult.getAllErrors().get(0);
            return new ExceptionResponse(e, HttpStatus.BAD_REQUEST.value(), objectError.getDefaultMessage(), enablePrintException());
        }
        return new ExceptionResponse(e, HttpStatus.BAD_REQUEST.value(), enablePrintException());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleMethodArgumentNotValidException(MethodArgumentTypeMismatchException e) {
        return new ExceptionResponse(e, HttpStatus.BAD_REQUEST.value(), enablePrintException());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBusinessException(BusinessException e) {
        log.debug(e.getMsg(), e);
        return new ExceptionResponse(e, Response.BUSINESS_ERROR_CODE, e.getMsg(), enablePrintException());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleDomainNotFoundException(DomainNotFoundException e, HttpServletRequest httpServletRequest) {
        log.warn("{} {} NOT FOUND: {}", httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), e.getSearchWord());
        return new ExceptionResponse(e, HttpStatus.NOT_FOUND.value(), enablePrintException());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handleUnauthorizedException(UnauthorizedException e) {
        return new ExceptionResponse(e, HttpStatus.UNAUTHORIZED.value(), enablePrintException());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleUnauthorizedException(ForbiddenException e) {
        return new ExceptionResponse(e, HttpStatus.FORBIDDEN.value(), enablePrintException());
    }

}
