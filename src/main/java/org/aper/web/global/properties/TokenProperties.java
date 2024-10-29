package org.aper.web.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security")
public class TokenProperties {

    private String authorizationHeader;
    private String authorizationKey;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
}
