package org.aper.web.global.config;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes
    ) throws Exception {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            String token = servletRequest.getServletRequest().getParameter("token");

            log.debug("WebSocket handshake - token: {}", token != null ? "present" : "absent");

            if (token != null && !token.isBlank()) {
                try {
                    if (token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    }

                    Claims claims = tokenProvider.parseClaims(token);
                    String email = claims.getSubject();

                     User user = userRepository.findByEmail(email)
                             .orElseThrow();

                    attributes.put("email", email);
                    attributes.put("userId", user.getUserId()); // TODO: 1L로 넣어서 사용하다가, userId() 추출하는 로직으로 사용

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
