package org.example.springaper.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.springaper.domain.user.dto.LoginRequestDto;
import org.example.springaper.domain.user.entity.UserRoleEnum;
import org.example.springaper.global.security.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")//로그인을 하고 성공하면 JWT 생성.
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter { // 그림 속 그 필터 usernamepassauth..fileter
    //session 방식이 아니라 , JWT 방식을 사용할 것이기 때문에 직접 custum 해서 사용한다.
    private final JwtUtil jwtUtil;//JWT 생성할거니까 jwtUtil 필요.

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");//우리가 지정한 login url이기 때문에. websecurityconfig에서 위치 설정해주던거 여기서 처리.
    }

    @Override // 재정의
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);//(무엇을 변환할 건지, 변환할 type)
            //objectmapper: json 형태의 data를 object로 만드는 부분.
            return getAuthenticationManager().authenticate( //원래 있는 getAuthenticationmanager이용 : 그림에도 있는 부분// authenticate: 인증 처리하는 메서드 <- 여기다가 token 전해줘야하마.
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null//authorities는 아직 안정해져서 null로 넣은건가? 아래에서 가져와서 넣어줌. role 부분
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override//로그인이 성공하면 이 메서드가 실행이 된다.
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();//PRINCIPLE: 박스 3개 중에 맨 앞에 있는거 정보 담는거 말함.
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(username, role);//role 가져오는 이유는 token 생성할 때 role 넣어주기로 했기 때문.
        jwtUtil.addJwtToCookie(token, response);//합쳐주는 메서드 ::이를 통해 토큰을 만들어줌.
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(401);
    }
}