package org.aper.web.global.handler;

import com.siot.IamportRestClient.exception.IamportResponseException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.handler.exception.TokenException;
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
import java.util.*;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletResponse response) throws IOException {
        Map<String, String> errorMap = new HashMap<>();
        BindingResult result = e.getBindingResult();

        for (FieldError error : result.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }

        log.error("handleMethodArgumentNotValidException", e);
        CustomResponseUtil.fail(response, "Invalid input value", errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(ConstraintViolationException e, HttpServletResponse response) throws IOException {
        Map<String, String> errorMap = new HashMap<>();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            String errorMessage = violation.getMessage();
            errorMap.put("validation error", errorMessage);
        }
        log.error("handleConstraintViolationException", e);
        CustomResponseUtil.fail(response, "Invalid input value", errorMap, HttpStatus.BAD_REQUEST);
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
        CustomResponseUtil.fail(response, e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(IOException.class)
    public void handleIOException(IOException e, HttpServletResponse response) {
        log.error("handleIOException", e);
        CustomResponseUtil.fail(response, "IO error", HttpStatus.INTERNAL_SERVER_ERROR);
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
        CustomResponseUtil.fail(response, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenException.class)
    public void handleTokenException(TokenException e, HttpServletResponse response) {
        log.error("handleTokenException", e);
        CustomResponseUtil.fail(response, e.getMessage(), e.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e, HttpServletResponse response) {
        log.error("handleException", e);
        CustomResponseUtil.fail(response, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) {
        log.error("handleIllegalArgumentException", e);
        CustomResponseUtil.fail(response, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
