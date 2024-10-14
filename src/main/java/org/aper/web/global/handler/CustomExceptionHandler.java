package org.aper.web.global.handler;

import com.siot.IamportRestClient.exception.IamportResponseException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.handler.exception.TokenException;
import org.aper.web.global.slack.SlackService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    private final SlackService slackService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletResponse response) {
        Map<String, String> errorMap = new HashMap<>();
        BindingResult result = e.getBindingResult();

        for (FieldError error : result.getFieldErrors()) {
            errorMap.put(simplifyFieldName(error.getField()), error.getDefaultMessage());
        }

        log.error("handleMethodArgumentNotValidException", e);
        CustomResponseUtil.fail(response, ErrorCode.INVALID_INPUT_VALUE, errorMap);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(ConstraintViolationException e, HttpServletResponse response) {
        Map<String, String> errorMap = new HashMap<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errorMap.put(simplifyFieldName(violation.getPropertyPath().toString()), violation.getMessage());
        }

        log.error("handleConstraintViolationException", e);
        CustomResponseUtil.fail(response, ErrorCode.INVALID_INPUT_VALUE, errorMap);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletResponse response) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        CustomResponseUtil.fail(response, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletResponse response) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        CustomResponseUtil.fail(response, e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException e, HttpServletResponse response) {
        log.error("handleAccessDeniedException", e);
        CustomResponseUtil.fail(response, e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IOException.class)
    public void handleIOException(IOException e, HttpServletResponse response) {
        log.error("handleIOException", e);
        CustomResponseUtil.fail(response, ErrorCode.IO_EXCEPTION);
    }

    @ExceptionHandler(IamportResponseException.class)
    public void handleIamPortResponseException(IamportResponseException e, HttpServletResponse response) {
        log.error("handleIamPortResponseException", e);
        CustomResponseUtil.fail(response, "IamPort response error", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public void handleMissingRequestCookieException(MissingRequestCookieException e, HttpServletResponse response) {
        log.error("handleMissingRequestCookieException", e);
        CustomResponseUtil.fail(response, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceException.class)
    public void handleServiceException(ServiceException e, HttpServletResponse response) {
        log.error("handleServiceException", e);
        CustomResponseUtil.fail(response, e.getMessage(), e.getStatus(), e.getCode());
    }

    @ExceptionHandler(TokenException.class)
    public void handleTokenException(TokenException e, HttpServletResponse response) {
        log.error("handleTokenException", e);
        CustomResponseUtil.fail(response, e.getMessage(), e.getStatus(), e.getCode());
    }

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e, HttpServletResponse response) {
        log.error("handleException", e);
        CustomResponseUtil.fail(response, ErrorCode.INTERNAL_SERVER_ERROR);
        slackService.sendErrorMessageToSlack("General Exception: " + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) {
        log.error("handleIllegalArgumentException", e);
        CustomResponseUtil.fail(response, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private String simplifyFieldName(String fieldName) {
        if (fieldName.contains(".")) {
            return fieldName.substring(fieldName.lastIndexOf(".") + 1);
        }
        return fieldName;
    }
}
