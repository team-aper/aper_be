package org.example.springaper.global.config;


import org.example.springaper.global.jwt.JwtAuthenticationFilter;
import org.example.springaper.global.jwt.JwtAuthorizationFilter;
import org.example.springaper.global.jwt.JwtUtil;
import org.example.springaper.global.security.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration; // bean에서 관리되는 아이.

    public WebSecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean//수동 등록 manager가 바로 가져올 수 없음. authenticationconfiguration을 가져와야지 사용가능하니까.//이것도 그림에 있는거. authenticationManager. configuration이 선언되어야 사용할 수 있기에 추후에 선언해줌. but 수동적으로
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager(); // 그림에 있는 녀석, username password-token 받고 인증해주는 아이 // 그냥 authenticationManager를 가지고 오는
    }

    @Bean//이것도 그림에 있는 아이. bean 으로 만들어주려고 이렇게 하는거. 새로 modifiy 해서 기능 만드는 거니까 bean도 수동으로 만들어줘야함.
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil); //this.util = util로 만들고... logininpage보이도록 반환해주는
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration)); // authenticationmanager도 그림에 있음.
        return filter;//filter.setAuthenticationManager은 인자로 authenticationManager를 필요로 함. authenticationmanager은 authenticaitonconfiguration을 필요로 함. 이는 위에서 선언되어 있음. 사용 가느,.
    }

    //위 아래 둘다 jwtauthenticationfilter와 jwtauthorization filter를 이용. jwt 폴더에 만들어둔거.
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean//위에서 우리가 filter를 만들었지, filter를 끼워넣어주지 않았음. filter를 끼워넣어주는 부분이 맨 아래 2줄임.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정 ****** 새로고침 할 때마다 json이 새로 생성되지 않도록 하는 것 : STATELESS
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .anyRequest().permitAll()
//                        .requestMatchers("/api/user/**").permitAll() // '/api/user/'로 시작하는 요청 모두 접근 허가
//                        .requestMatchers("/chat/**").permitAll()
//                        //.requestMatchers("/send/**").permitAll()
//                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        // 필터 관리
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);// 우리가 만든 jwtfilter 를 원래 있던 필터 앞에 놓는다.
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        //인가 -> 인가가 제대로 안되면 로그인 진행 -> 로그인 /// authorization -> authentication

        //접근 불가 페이지
        http.exceptionHandling((exceptionHandling) ->
                exceptionHandling.accessDeniedPage("/forbidden.html")
        );


        return http.build();
    }
}
