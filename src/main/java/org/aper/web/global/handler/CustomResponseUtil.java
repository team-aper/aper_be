package org.aper.web.global.handler;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomResponseUtil {


    // 성공시 응답 처리 메소드
    public static void success(HttpServletResponse response, String message, HttpStatus status) {
        log.info("CustomResponseUtil.success called with message: {}, status: {}", message, status);
        responseWithMessage(response, message, status);
    }


    public static void success(HttpServletResponse response, String message, Map<String, Object> data, HttpStatus status) {
        log.info("CustomResponseUtil.success called with message: {}, data: {}, status: {}", message, data, status);
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write("{\"message\": \"" + message + "\", \"status\": " + status.value() + ", \"data\": " + data + "}");
        } catch (IOException e) {
            log.error("Error writing response", e);
        }
    }

    public static void fail(HttpServletResponse response, String message, HttpStatus status) {
        log.info("CustomResponseUtil.fail called with message: {}, status: {}", message, status);
        responseWithMessage(response, message, status);
    }

    public static void fail(HttpServletResponse response, String message, Map<String, String> errors, HttpStatus status) {
        log.info("CustomResponseUtil.fail called with message: {}, errors: {}, status: {}", message, errors, status);
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); // 인코딩 설정 추가
        try (PrintWriter writer = response.getWriter()) {
            writer.write("{\"message\": \"" + message + "\", \"status\": " + status.value() + ", \"errors\": " + errors + "}");
        } catch (IOException e) {
            log.error("Error writing response", e);
        }
    }

    private static void responseWithMessage(HttpServletResponse response, String message, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write("{\"message\": \"" + message + "\", \"status\": " + status.value() + "}");
        } catch (IOException e) {
            log.error("Error writing response", e);
        }
    }
}
