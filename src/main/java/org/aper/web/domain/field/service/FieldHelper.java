package org.aper.web.domain.field.service;

import org.aper.web.domain.user.entity.User;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Component;

@Component
public class FieldHelper {

    public boolean isOwnField(Long authorId, UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return false;
        }
        User user = userDetails.user();
        Long userId = user.getUserId();
        return userId.equals(authorId);
    }
}
