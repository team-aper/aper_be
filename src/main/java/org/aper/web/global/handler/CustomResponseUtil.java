package org.aper.web.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class CustomResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void success(HttpServletResponse response, String message, HttpStatus status) {
        log.info("CustomResponseUtil.success called with message: {}, status: {}", message, status);
        responseWithMessage(response, message, status, null, null);
    }

    public static void success(HttpServletResponse response, String message, Map<String, Object> data, HttpStatus status) {
        log.info("CustomResponseUtil.success called with message: {}, data: {}, status: {}", message, data, status);
        responseWithMessage(response, message, status, data, null);
    }

    public static void fail(HttpServletResponse response, String message, HttpStatus status) {
        log.info("CustomResponseUtil.fail called with message: {}, status: {}", message, status);
        responseWithMessage(response, message, status, null, null);
    }

    public static void fail(HttpServletResponse response, ErrorCode errorCode) {
        log.info("CustomResponseUtil.fail called with message: {}, errorCode: {}", errorCode.getMessage(), errorCode.getCode());
        responseWithMessage(response, errorCode.getMessage(), errorCode.getStatus(), null, errorCode.getCode());
    }

    public static void fail(HttpServletResponse response, String message, HttpStatus status, String code) {
        log.info("CustomResponseUtil.fail called with message: {}, status: {}, code: {}", message, status, code);
        responseWithMessage(response, message, status, null, code);
    }

    public static void fail(HttpServletResponse response, ErrorCode errorCode, Map<String, String> errors) {
        log.info("CustomResponseUtil.fail called with message: {}, errors: {}, status: {}", errorCode.getMessage(), errors, errorCode.getCode());
        responseWithErrorMessage(response, errorCode.getMessage(), errorCode.getStatus(), errors);
    }

    private static void responseWithMessage(HttpServletResponse response, String message, HttpStatus status, Object jsonData, String code) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = response.getWriter()) {
            Map<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("status", status.value());
            if (code != null) {
                responseMap.put("code", code);
            }
            if (jsonData != null) {
                responseMap.put("data", jsonData);
            }
            responseMap.put("message", message);

            String jsonResponse = objectMapper.writeValueAsString(responseMap);
            writer.write(jsonResponse);
        } catch (IOException e) {
            log.error("Error writing response", e);
        }
    }

    private static void responseWithErrorMessage(HttpServletResponse response, String message, HttpStatus status, Map<String, String> errors) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = response.getWriter()) {
            Map<String, Object> responseMap = new LinkedHashMap<>();
            responseMap.put("status", status.value());
            responseMap.put("data", errors);
            responseMap.put("message", message);

            String jsonResponse = objectMapper.writeValueAsString(responseMap);
            writer.write(jsonResponse);
        } catch (IOException e) {
            log.error("Error writing response", e);
        }
    }

    private static String convertObjectToJson(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (IOException e) {
            log.error("Error converting object to JSON", e);
            return "{}";
        }
    }
}
