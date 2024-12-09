package org.aper.web.global.config;

import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.authHandler.CustomAccessDeniedHandler;
import org.aper.web.global.handler.authHandler.CustomAuthenticationEntryPoint;
import org.aper.web.global.handler.authHandler.CustomAuthenticationFailureHandler;
import org.aper.web.global.handler.authHandler.OAuth2AuthenticationSuccessHandler;
import org.aper.web.global.jwt.TokenProvider;
import org.aper.web.global.jwt.service.LogoutService;
import org.aper.web.global.oauth2.CustomOAuth2UserService;
import org.aper.web.global.oauth2.CustomRequestEntityConverter;
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
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
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

        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080"));
        configuration.addAllowedOriginPattern("https://*.aper.cc");
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Set-Cookie", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
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
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> authorizationCodeTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient tokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();

        tokenResponseClient.setRequestEntityConverter(request -> {
            if ("kakao".equals(request.getClientRegistration().getRegistrationId()) || "naver".equals(request.getClientRegistration().getRegistrationId())) {
                return new CustomRequestEntityConverter().convert(request);
            }
            return new OAuth2AuthorizationCodeGrantRequestEntityConverter().convert(request);
        });
        return tokenResponseClient;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        );

        http.oauth2Login(oauth2Login ->
                oauth2Login
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler)
                        .tokenEndpoint(tokenEndpoint ->
                                tokenEndpoint.accessTokenResponseClient(authorizationCodeTokenResponseClient())
                        )
                        .userInfoEndpoint(userInfoEndpoint ->
                                userInfoEndpoint.userService(customOAuth2UserService())
                        )
        );

        // 시큐리티 CORS 설정
        http.cors(cors -> cors.configurationSource(configurationSource()));

        // 권한에 따른 접근 설정
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(AuthenticatedMatchers.flexiblePathArray).permitAll()
                        .requestMatchers(AuthenticatedMatchers.swaggerArray).permitAll()
                        .requestMatchers(AuthenticatedMatchers.excludedPathArray).permitAll()
                        .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.formLogin(AbstractHttpConfigurer::disable);

        http.logout(logoutConfig -> logoutConfig
                .logoutUrl("/logout")
                .addLogoutHandler(logoutService)
                .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
        );

        // 예외 처리 설정
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint) // 인증 실패 처리
                        .accessDeniedHandler(accessDeniedHandler) // 인가 실패 처리
        );

        return http.build();
    }

}
