package org.aper.web.global.config;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.jwt.TokenProvider;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {
    private final TokenProvider tokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes
    ) throws Exception {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            // 1. Query Parameter에서 JWT 추출
            String token = servletRequest.getServletRequest().getParameter("token");

            log.debug("WebSocket handshake - token: {}", token != null ? "present" : "absent");

            if (token != null && !token.isBlank()) {
                try {
                    // 2. Bearer prefix 제거
                    if (token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    }

                    // 3. 토큰 검증 및 Claims 추출
                    Claims claims = tokenProvider.parseClaims("Bearer " + token);
                    String email = claims.getSubject();

                    // 4. email로 userId 조회가 필요하면 여기서 처리
                    // User user = userRepository.findByEmail(email).orElseThrow(...);
                    // Long userId = user.getUserId();

                    // 임시: email을 userId로 사용 (실제로는 DB 조회 필요)
                    // TODO: email로 userId 조회하는 로직 추가
                    attributes.put("email", email);
                    attributes.put("userId", 1L); // 임시 하드코딩

                    log.info("WebSocket authenticated - email: {}", email);
                    return true;

                } catch (Exception e) {
                    log.warn("WebSocket authentication failed - error: {}", e.getMessage());
                    return false;
                }
            }

            log.warn("WebSocket handshake rejected - no token provided");
            return false;
        }

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // Implement if necessary
    }

}
