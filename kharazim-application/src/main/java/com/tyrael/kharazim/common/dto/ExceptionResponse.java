package com.tyrael.kharazim.common.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Tyrael Archangel
 * @since 2023/12/22
 */
@EqualsAndHashCode(callSuper = true)
public class ExceptionResponse extends Response {

    @Getter
    private final String exception;

    public ExceptionResponse(Exception e, int code, String msg, Boolean enablePrintExceptionStackTrace) {
        this.markError(code, msg);
        if (Boolean.TRUE.equals(enablePrintExceptionStackTrace)) {
            this.exception = exceptionStackTrace(e);
        } else {
            this.exception = e == null ? null : e.getClass().getName();
        }
    }

    public ExceptionResponse(Exception e, int code, Boolean enablePrintExceptionStackTrace) {
        this(e, code, e == null ? null : e.getMessage(), enablePrintExceptionStackTrace);
    }

    public static String exceptionStackTrace(Exception e) {
        if (e != null) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(os));
            return os.toString(StandardCharsets.UTF_8);
        }
        return null;
    }

}
