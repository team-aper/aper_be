package org.aper.web.global.security.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.config.AuthenticatedMatchers;
import org.aper.web.global.handler.CustomResponseUtil;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.TokenException;
import org.aper.web.global.jwt.TokenProvider;
import org.aper.web.global.security.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(TokenProvider tokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        Set<String> swaggerPaths = new HashSet<>(Arrays.asList(AuthenticatedMatchers.swaggerArray));
        Set<String> excludedPaths = new HashSet<>(Arrays.asList(AuthenticatedMatchers.excludedPathArray));
        return swaggerPaths.contains(path) || excludedPaths.contains(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenValue = tokenProvider.getJwtFromHeader(request);

        if (StringUtils.hasText(tokenValue)) {
            try{
            String accessToken = tokenProvider.getJwtFromHeader(request);
            if (accessToken != null) {
                Claims claims = tokenProvider.getUserInfoFromAccessToken(accessToken);
                if (claims.getExpiration().before(new Date())){
                    CustomResponseUtil.fail(response, ErrorCode.EXPIRED_ACCESS_TOKEN.getMessage(), HttpStatus.UNAUTHORIZED);
                }
                String username = claims.getSubject();
                if (username != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (TokenException e) {
                log.error(e.getMessage());
                SecurityContextHolder.clearContext();
                CustomResponseUtil.fail(response, e.getMessage(), e.getStatus());
        }
        filterChain.doFilter(request, response);

        }

    }
}
