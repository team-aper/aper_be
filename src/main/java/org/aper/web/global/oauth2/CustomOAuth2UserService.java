package org.aper.web.global.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.aper.web.global.security.UserDetailsServiceImpl;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserDetailsServiceImpl userDetailsService;

    public CustomOAuth2UserService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.debug("OAuth2 state parameter: {}", userRequest.getAdditionalParameters().get("state"));
        log.debug("OAuth2 Request Additional Parameters: {}", userRequest.getAdditionalParameters());
        log.debug("Client Registration Id: {}", userRequest.getClientRegistration().getRegistrationId());
        log.debug("User Name Attribute Name: {}", userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());
        log.debug("Access Token: {}", userRequest.getAccessToken().getTokenValue());

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();

        log.debug("OAuth2 Provider: {}", registrationId);
        log.debug("User Attributes: {}", memberAttribute);

        String email = (String) memberAttribute.get("email");

        if (email == null) {
            log.error("Email attribute is missing in OAuth2 response: {}", oAuth2User.getAttributes());
            throw new ServiceException(ErrorCode.INVALID_FORMAT_REQUEST, "Email attribute is required for OAuth2 authentication.");
        }

        return (UserDetailsImpl) userDetailsService.loadUserByUsername(email, memberAttribute);
    }
}
