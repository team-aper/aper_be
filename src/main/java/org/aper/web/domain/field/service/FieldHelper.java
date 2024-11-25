package org.aper.web.domain.field.service;

import com.aperlibrary.subscription.entity.Subscription;
import com.aperlibrary.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.subscription.repository.SubscriptionRepository;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FieldHelper {
    private final SubscriptionRepository subscriptionRepository;

    public boolean isOwnField(Long authorId, UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return false;
        }
        User user = userDetails.user();
        Long userId = user.getUserId();
        return userId.equals(authorId);
    }

    public boolean isSubscribed(Long authorId, UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return false;
        }
        Long userId = userDetails.user().getUserId();
        Optional<Subscription> subscription = subscriptionRepository.findByAuthorUserIdAndSubscriberUserId(authorId, userId);
        return subscription.isPresent();
    }

}
