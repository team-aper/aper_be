package org.aper.web.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.dto.ErrorResponseDto;
import org.aper.web.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class CustomResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void success(HttpServletResponse response, String message, HttpStatus status) {
        log.info("CustomResponseUtil.success called with message: {}, status: {}", message, status);
        ResponseDto<Void> successResponse = ResponseDto.success(message);
        writeResponse(response, successResponse, status);
    }

    public static void fail(HttpServletResponse response, ErrorCode errorCode) {
        getInfo(errorCode.getCode(), errorCode.getMessage());
        ErrorResponseDto errorResponse = new ErrorResponseDto(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage());
        writeResponse(response, errorResponse, errorCode.getStatus());
    }

    public static void fail(HttpServletResponse response, HttpStatus status, String code, String message) {
        getInfo(code, message);
        ErrorResponseDto errorResponse = new ErrorResponseDto(status.value(), code, message);
        writeResponse(response, errorResponse, status);
    }

    public static String extractMessages(Map<String, String> errorMap) {
        return String.join(", ", errorMap.values());
    }

    private static void getInfo(String code, String message) {
        log.info("CustomResponseUtil.fail called with code: {}, message: {}", code, message);
    }

    private static void writeResponse(HttpServletResponse response, Object responseDto, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = response.getWriter()) {
            String jsonResponse = objectMapper.writeValueAsString(responseDto);
            writer.write(jsonResponse);
        } catch (IOException e) {
            log.error("Error writing response", e);
        }
    }
}
