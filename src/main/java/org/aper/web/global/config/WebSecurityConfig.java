package org.aper.web.global.config;

import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.authHandler.CustomAccessDeniedHandler;
import org.aper.web.global.handler.authHandler.CustomAuthenticationEntryPoint;
import org.aper.web.global.handler.authHandler.CustomAuthenticationFailureHandler;
import org.aper.web.global.jwt.TokenProvider;
import org.aper.web.global.jwt.service.LogoutService;
import org.aper.web.global.oauth2.CustomOAuth2UserService;
import org.aper.web.global.handler.authHandler.OAuth2AuthenticationSuccessHandler;
import org.aper.web.global.security.UserDetailsServiceImpl;
import org.aper.web.global.security.filter.JwtAuthorizationFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    public final LogoutService logoutService;
    public final UserRepository userRepository;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    public final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    public WebSecurityConfig(TokenProvider tokenProvider, UserDetailsServiceImpl userDetailsService, LogoutService logoutService, UserRepository userRepository, CustomAuthenticationEntryPoint authenticationEntryPoint, CustomAccessDeniedHandler accessDeniedHandler, CustomAuthenticationFailureHandler customAuthenticationFailureHandler, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.logoutService = logoutService;
        this.userRepository = userRepository;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
    }

    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(
                List.of("Authorization", "Set-Cookie", "Cache-Control", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService(userRepository);
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(tokenProvider, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.oauth2Login(oauth2Login ->
                oauth2Login
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler)
                        .userInfoEndpoint(userInfoEndpoint ->
                                userInfoEndpoint.userService(customOAuth2UserService())
                        )
        );

        // 시큐리티 CORS 빈 설정
        http.cors((cors) -> cors.configurationSource(configurationSource()));

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(AuthenticatedMatchers.flexiblePathArray).permitAll()
                        .requestMatchers(AuthenticatedMatchers.swaggerArray).permitAll()
                        .requestMatchers(AuthenticatedMatchers.excludedPathArray).permitAll().anyRequest().authenticated()
        );

        http.formLogin(formLogin ->
                formLogin
                        .failureHandler(customAuthenticationFailureHandler)
        );

        http.logout(logoutConfig -> logoutConfig
                        .logoutUrl("/logout") // 로그아웃 처리할 URL 지정
                        .addLogoutHandler(logoutService)
                        .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
                );

        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 예외 처리 설정
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint) // 인증 실패 처리
                        .accessDeniedHandler(accessDeniedHandler) // 인가 실패 처리
        );

        return http.build();
    }
}
