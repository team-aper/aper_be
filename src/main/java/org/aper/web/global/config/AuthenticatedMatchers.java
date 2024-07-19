package org.aper.web.global.config;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthenticatedMatchers {
    public static final String[] swaggerArray = {
            "/api-docs",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/api-docs/**",
    };
}