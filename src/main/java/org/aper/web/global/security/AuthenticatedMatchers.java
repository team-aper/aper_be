package org.aper.web.global.security;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthenticatedMatchers {
    public static final String[] swaggerArray = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api-docs/**",
            "/v3/api-docs/**"
    };
    public static final String[] excludedPathArray = {
            "/reissue",
            "/signup",
            "/login",
            "/health",
            "/email/*",
            "/password/*",
            "/logout"
    };
    public static final String[] flexiblePathArray = {
            "/field/**",
    };
}