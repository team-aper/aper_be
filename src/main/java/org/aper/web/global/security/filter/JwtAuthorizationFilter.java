package org.aper.web.global.security.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.handler.CustomResponseUtil;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.TokenException;
import org.aper.web.global.jwt.TokenProvider;
import org.aper.web.global.security.UserDetailsServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(TokenProvider tokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // 재발급 요청일 경우 필터를 통과시킴
        if ("/reissue".equals(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String tokenValue = tokenProvider.getJwtFromHeader(request);

        if (StringUtils.hasText(tokenValue)) {
            try{
                Claims claims = tokenProvider.getUserInfoFromAccessToken(tokenValue);
                if (claims.getExpiration().before(new Date())){
                    CustomResponseUtil.fail(response, ErrorCode.EXPIRED_ACCESS_TOKEN.getMessage(), HttpStatus.UNAUTHORIZED);
                    return;
                }
                String username = claims.getSubject();
                if (username != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
            } catch (TokenException e) {
                log.error(e.getMessage());
                SecurityContextHolder.clearContext();
                CustomResponseUtil.fail(response, e.getMessage(), e.getStatus());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
