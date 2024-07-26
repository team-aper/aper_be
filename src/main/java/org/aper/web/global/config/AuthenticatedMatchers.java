package org.aper.web.global.config;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthenticatedMatchers {
    public static final String[] swaggerArray = {
            "/swagger-ui/index.html",
            "/swagger-ui/index.css",
            "/swagger-ui/swagger-ui.css",
            "/swagger-ui/swagger-ui-bundle.js",
            "/swagger-ui/swagger-ui-standalone-preset.js",
            "/swagger-ui/swagger-initializer.js",
            "/api-docs/swagger-config",
            "/swagger-ui/favicon-32x32.png",
            "/api-docs"
    };
    public static final String[] excludedPathArray = {
            "/reissue",
            "/signup",
            "/login"
    };
}