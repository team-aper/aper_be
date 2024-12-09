package org.aper.web.global.config;

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
            "/user/signup",
            "/login",
            "/health",
            "/user/email/*",
            "/user/password/*",
            "/logout",
            "/curation/main",
            "/main/*",
            "/search/*",
            "/scheduler/test",
            "/auth/me"
    };
    public static final String[] flexiblePathArray = {
            "/field/**",
            "/story/**",
            "/episode/**",
            "/user/tutors",
            "/login/oauth2/code/**",
            "/oauth2/**"
    };
}