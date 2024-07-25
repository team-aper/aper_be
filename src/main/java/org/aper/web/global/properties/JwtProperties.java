package org.aper.web.global.properties;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt.secret.key")
public class JwtProperties {

    @Bean
    public Key accessKey() {
        byte[] accessBytes = Base64.getDecoder().decode(this.access);
        return Keys.hmacShaKeyFor(accessBytes);
    }

    @Bean
    public Key refreshKey() {
        byte[] refreshBytes = Base64.getDecoder().decode(this.refresh);
        return Keys.hmacShaKeyFor(refreshBytes);
    }

    private String access;
    private String refresh;
}