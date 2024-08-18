package org.aper.web.global.handler.authHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.handler.CustomResponseUtil;
import org.aper.web.global.handler.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) {
        log.error("인증 실패: {}", exception.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put("errorCode", ErrorCode.AUTHENTICATION_FAILED.getCode());
        errors.put("message", exception.getMessage());

        CustomResponseUtil.fail(response, ErrorCode.AUTHENTICATION_FAILED.getMessage(), errors, HttpStatus.BAD_REQUEST);
    }
}
