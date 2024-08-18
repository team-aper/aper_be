package org.aper.web.global.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
        errors.put("errorCode", ErrorCode.OAUTH2_AUTHENTICATION_FAIL.name());
        errors.put("cause", exception.getMessage());

        CustomResponseUtil.fail(response, ErrorCode.OAUTH2_AUTHENTICATION_FAIL.getMessage(), errors, HttpStatus.BAD_REQUEST);
    }
}
