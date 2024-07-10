package org.example.springaper.global.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springaper.domain.user.entity.UserRoleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth"; // 사용자 권한에 대한 정보 권한을 구분하기 위한 key 값으로 authorization_key를
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer "; //만들 토큰 앞에 붙이는. 규칙. 해당 값이 토큰임을 알려주는 느낌쓰
    // 구분하기 위해서 한칸 뛰어야 함.
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분 ms 단위

    @Value("${secret.key.access}") // Base64 Encode 한 SecretKey -> application에서 값 가져오는, @Value이용해서 가져옴.
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //이 알고리즘 사용할 것임.

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");//로그 기억하기 위해

    @PostConstruct // 후에 construct 하는 녀석. secretkey 이런거 가져오고 만들어야 하니까.
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);//base64 디코드 즉, 암호화 시킨거임.
        key = Keys.hmacShaKeyFor(bytes);// byte를 key로 변환.
    }

    // 토큰 생성
    public String createToken(String username, UserRoleEnum role) {//UserRoleEnum에는 권한 값이 들어 있음.
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }


    // JWT Cookie 에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value // create Token에서 생성한 token이랑, 인식 느낌의 키 넣어줌.
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            res.addCookie(cookie); //반환되는 곳인 res에다가 값 저장
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    //생성된 JWT를 쿠키에 저장 -> 서버에 쿠키 정보를 만들면 만료 등을 할 수 있다는 점에서 이점. header에 넣는 것은,

    // 쿠키에 들어 있던 JWT 토큰을 Substring
    // JWT 토큰 substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) { //공백과 널이 아닌지 확인 && 토큰은 bearer로 시작하니까, bearer로 시작되는지
            return tokenValue.substring(7);// bearer이 공백포함 7자니까 잘라줌.
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }


    //JWT 검증
    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); //토큰의 위변조 검증
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    //JWT에서 사용자 정보 가져오기
    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody(); //getBody를 통해서 Claims 가져옴. Claim 기반 web token 이니까
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

}