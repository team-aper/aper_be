package org.aper.web.global.oauth2;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {

    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String name;
    private String provider;

    public static OAuth2Attribute of(String provider, String attributeKey,
                                     Map<String, Object> attributes) {
        return switch (provider) {
            case "google" -> ofGoogle(provider, attributeKey, attributes);
            case "kakao" -> ofKakao(provider, attributes);
            case "naver" -> ofNaver(provider, attributes);
            default -> throw new IllegalArgumentException("Invalid provider: " + provider);
        };
    }

    private static OAuth2Attribute ofGoogle(String provider, String attributeKey,
                                            Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .provider(provider)
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }

    private static OAuth2Attribute ofKakao(String provider,
                                           Map<String, Object> attributes) {
        Object kakaoAccountObj = attributes.get("kakao_account");
        if (!(kakaoAccountObj instanceof Map)) {
            throw new IllegalArgumentException("Invalid kakao_account format in attributes");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoAccountObj;

        Object profileObj = kakaoAccount.get("profile");
        if (!(profileObj instanceof Map)) {
            throw new IllegalArgumentException("Invalid profile format in kakao_account");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> profile = (Map<String, Object>) profileObj;

        return OAuth2Attribute.builder()
                .email((String) kakaoAccount.get("email"))
                .name((String) profile.get("nickname"))
                .provider(provider)
                .attributes(kakaoAccount)
                .attributeKey("email")
                .build();
    }

    private static OAuth2Attribute ofNaver(String provider,
                                           Map<String, Object> attributes) {
        Object responseObj = attributes.get("response");
        if (!(responseObj instanceof Map)) {
            throw new IllegalArgumentException("Invalid response format in attributes");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) responseObj;

        return OAuth2Attribute.builder()
                .email((String) response.get("email"))
                .name((String) response.get("name"))
                .attributes(response)
                .provider(provider)
                .attributeKey("id")
                .build();
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("name", name);
        map.put("key", attributeKey);
        map.put("email", email);
        map.put("provider", provider);

        return map;
    }
}
