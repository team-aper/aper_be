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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    private final SlackService slackService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletResponse response) {
        BindingResult result = e.getBindingResult();
        FieldError firstError = result.getFieldError();

        if (firstError != null) {
            String errorMessage = firstError.getDefaultMessage();
            CustomResponseUtil.fail(response, HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT_VALUE.getCode(), errorMessage);
        } else {
            CustomResponseUtil.fail(response, ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(ConstraintViolationException e, HttpServletResponse response) {
        Map<String, String> errorMap = new HashMap<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errorMap.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        log.error("handleConstraintViolationException", e);

        String extractedMessage = CustomResponseUtil.extractMessages(errorMap);
        CustomResponseUtil.fail(response, ErrorCode.INVALID_INPUT_VALUE.getStatus(), ErrorCode.INVALID_INPUT_VALUE.getCode(), extractedMessage);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletResponse response) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        CustomResponseUtil.fail(response, HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT_VALUE.getCode(), e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletResponse response) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        CustomResponseUtil.fail(response, HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException e, HttpServletResponse response) {
        log.error("handleAccessDeniedException", e);
        CustomResponseUtil.fail(response, HttpStatus.FORBIDDEN, "ACCESS_DENIED", e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public void handleIOException(IOException e, HttpServletResponse response) {
        log.error("handleIOException", e);
        CustomResponseUtil.fail(response, ErrorCode.IO_EXCEPTION);
    }

    @ExceptionHandler(IamportResponseException.class)
    public void handleIamPortResponseException(IamportResponseException e, HttpServletResponse response) {
        log.error("handleIamPortResponseException", e);
        CustomResponseUtil.fail(response, HttpStatus.BAD_REQUEST, "IAMPORT_RESPONSE_ERROR", "IamPort response error");
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public void handleMissingRequestCookieException(MissingRequestCookieException e, HttpServletResponse response) {
        log.error("handleMissingRequestCookieException", e);
        CustomResponseUtil.fail(response, HttpStatus.BAD_REQUEST, "MISSING_COOKIE", e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public void handleServiceException(ServiceException e, HttpServletResponse response) {
        log.error("handleServiceException", e);
        CustomResponseUtil.fail(response, e.getStatus(), e.getCode(), e.getMessage());
    }

    @ExceptionHandler(TokenException.class)
    public void handleTokenException(TokenException e, HttpServletResponse response) {
        log.error("handleTokenException", e);
        CustomResponseUtil.fail(response, e.getStatus(), e.getCode(), e.getMessage());
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
        CustomResponseUtil.fail(response, HttpStatus.BAD_REQUEST, "ILLEGAL_ARGUMENT", e.getMessage());
    }
}
