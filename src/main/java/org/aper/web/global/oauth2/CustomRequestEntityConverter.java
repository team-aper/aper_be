package org.aper.web.global.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collections;

@Slf4j
public class CustomRequestEntityConverter extends OAuth2AuthorizationCodeGrantRequestEntityConverter {

    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest request) {
        RequestEntity<?> originalRequest = super.convert(request);

        if (originalRequest == null) {
            throw new ServiceException(ErrorCode.REQUEST_IS_NULL);
        }

        // 기존 헤더 복사 및 수정
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(originalRequest.getHeaders());

        // Content-Type 및 Accept 설정
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // 기존 요청 본문을 가져와 필요한 파라미터 수정
        RequestEntity<?> modifiedRequest = getModifiedRequest(request, originalRequest, headers);

        // 로그 출력
        log.debug("Modified Request: {}", modifiedRequest);

        return modifiedRequest;
    }

    @NotNull
    private RequestEntity<?> getModifiedRequest(OAuth2AuthorizationCodeGrantRequest request, RequestEntity<?> originalRequest, HttpHeaders headers) {
        @SuppressWarnings("unchecked")
        LinkedMultiValueMap<String, String> body = (LinkedMultiValueMap<String, String>) originalRequest.getBody();

        // 클라이언트 인증 정보를 요청 파라미터로 추가
        if (body == null) {
            body = new LinkedMultiValueMap<>();
        }

        body.set("client_id", request.getClientRegistration().getClientId());
        body.set("client_secret", request.getClientRegistration().getClientSecret());

        // 수정된 본문과 헤더로 새로운 RequestEntity 생성
        return new RequestEntity<>(
                body,
                headers,
                originalRequest.getMethod(),
                originalRequest.getUrl()
        );
    }
}
