package org.aper.web.global.oauth2;

import com.aperlibrary.user.entity.User;
import com.aperlibrary.user.entity.constant.UserRoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.debug("OAuth2 state parameter: {}", userRequest.getAdditionalParameters().get("state"));
        log.debug("OAuth2 Request Additional Parameters: {}", userRequest.getAdditionalParameters());
        log.debug("Client Registration Id: {}", userRequest.getClientRegistration().getRegistrationId());
        log.debug("User Name Attribute Name: {}", userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());
        log.debug("Access Token: {}", userRequest.getAccessToken().getTokenValue());

        // 기본 OAuth2UserService 객체 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 클라이언트 등록 ID와 사용자 이름 속성 가져오기
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();

        log.debug("OAuth2 Provider: {}", registrationId);
        log.debug("User Attributes: {}", memberAttribute);

        // 사용자 이메일 정보 가져오기
        String email = (String) memberAttribute.get("email");

        if (email == null) {
            log.error("Email attribute is missing in OAuth2 response: {}", oAuth2User.getAttributes());
            throw new ServiceException(ErrorCode.INVALID_FORMAT_REQUEST, "Email attribute is required for OAuth2 authentication.");
        }

        Optional<User> findMember = userRepository.findByEmail(email);

        if (findMember.isEmpty()) {
            log.debug("No existing user found, creating new user with email: {}", email);

            User newUser = User.builder()
                    .email(email)
                    .password(UUID.randomUUID().toString()) // 임시 비밀번호 설정
                    .penName((String) memberAttribute.get("name"))
                    .role(UserRoleEnum.USER) // 기본 권한 설정
                    .build();

            log.debug("Attempting to save new user: {}", newUser);
            userRepository.save(newUser);
            log.info("New user created successfully: {}", newUser);

            memberAttribute.put("exist", true);
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority(UserRoleEnum.USER.getAuthority())),
                    memberAttribute, "email");
        }

        // 기존 회원이 존재할 경우 권한 가져오기
        User existingUser = findMember.get();
        String userAuthority = existingUser.getRole().getAuthority();

        log.debug("User found: {}", existingUser);
        memberAttribute.put("exist", true);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(userAuthority)),
                memberAttribute, "email");
    }

}
