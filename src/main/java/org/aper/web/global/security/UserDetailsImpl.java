package org.aper.web.global.security;

import com.aperlibrary.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public record UserDetailsImpl(User user, Map<String, Object> attributes) implements UserDetails, OAuth2User {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getAuthority()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getEmail();
    }

    // 정적 팩토리 메서드: 기본 로그인
    public static UserDetailsImpl create(User user) {
        return new UserDetailsImpl(user, null);
    }

    // 정적 팩토리 메서드: OAuth2 로그인
    public static UserDetailsImpl create(User user, Map<String, Object> attributes) {
        return new UserDetailsImpl(user, attributes);
    }
}