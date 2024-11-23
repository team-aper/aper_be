package org.aper.web.global.oauth2;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;

import java.util.HashMap;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Slf4j
public class OAuth2Attribute {

    private final Map<String, Object> attributes;
    private final String attributeKey;
    private final String email;
    private final String name;
    private final String provider;

    public static OAuth2Attribute of(String provider, String attributeKey,
                                     Map<String, Object> attributes) {
        return switch (provider) {
            case "google" -> ofGoogle(provider, attributeKey, attributes);
            case "kakao" -> ofKakao(provider, attributes);
            case "naver" -> ofNaver(provider, attributes);
            default -> throw new ServiceException(ErrorCode.INVALID_PROVIDER_REQUEST, provider);
        };
    }

    private static OAuth2Attribute ofGoogle(String provider, String attributeKey,
                                            Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.getOrDefault("name", "무명"))
                .provider(provider)
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }

    private static OAuth2Attribute ofKakao(String provider,
                                           Map<String, Object> attributes) {
        Object kakaoAccountObj = attributes.get("kakao_account");
        if (!(kakaoAccountObj instanceof Map)) {
            throw new ServiceException(ErrorCode.INVALID_FORMAT_REQUEST, provider);
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoAccountObj;

        Object profileObj = kakaoAccount.get("profile");
        if (!(profileObj instanceof Map)) {
            throw new ServiceException(ErrorCode.INVALID_FORMAT_REQUEST, provider);
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> profile = (Map<String, Object>) profileObj;

        String id = String.valueOf(attributes.get("id"));

        return OAuth2Attribute.builder()
                .email((String) kakaoAccount.get("email"))
                .name((String) profile.getOrDefault("nickname", "무명"))
                .provider(provider)
                .attributes(kakaoAccount)
                .attributeKey(id)
                .build();
    }

    private static OAuth2Attribute ofNaver(String provider,
                                           Map<String, Object> attributes) {
        Object responseObj = attributes.get("response");
        if (!(responseObj instanceof Map)) {
            throw new ServiceException(ErrorCode.INVALID_FORMAT_REQUEST);
        }

        log.debug("Naver API Response: {}", responseObj);

        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) responseObj;

        log.debug("Naver response: {}", response);

        String email = (String) response.get("email");

        String id = String.valueOf(response.get("id"));

        if (id == null || id.isEmpty()) {
            throw new ServiceException(ErrorCode.INVALID_FORMAT_REQUEST, "Naver response does not contain id");
        }

        return OAuth2Attribute.builder()
                .email(email)
                .name((String) response.getOrDefault("name", "무명"))
                .attributes(response)
                .provider(provider)
                .attributeKey(id)
                .build();
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("name", name);
        map.put("key", attributeKey);
        map.put("email", email);
        map.put("provider", provider);
        map.put("attributes", attributes);
        return map;
    }
}
