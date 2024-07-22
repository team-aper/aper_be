package org.aper.web.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;

@Slf4j
public class CustomResponseUtil {

    public static void success(HttpServletResponse response, Object dto) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDto = ResponseDto.success("로그인 성공", dto);
            String responseBody = om.writeValueAsString(responseDto);

            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().print(responseBody);
        } catch (Exception e) {
            log.error("서버 파싱 에러", e);
        }
    }

    public static void fail(HttpServletResponse response, String message, HttpStatus httpStatus) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<Object> responseDto = ResponseDto.fail(message);
            String responseBody = om.writeValueAsString(responseDto);

            response.setContentType("application/json; charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().print(responseBody);
        } catch (Exception e) {
            log.error("서버 파싱 에러", e);
        }
    }

    public static void fail(HttpServletResponse response, String message, Object data, HttpStatus httpStatus) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<Object> responseDto = ResponseDto.fail(message, data);
            String responseBody = om.writeValueAsString(responseDto);

            response.setContentType("application/json; charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().print(responseBody);
        } catch (Exception e) {
            log.error("서버 파싱 에러", e);
        }
    }
}
