package org.aper.web.domain.subscription.service;

import org.aper.web.domain.subscription.entity.Subscription;
import org.aper.web.domain.subscription.repository.SubscriptionRepository;
import org.aper.web.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    // 구독한 작가 목록을 조회
    public List<User> getSubscribedAuthors(Long subscriberId) {
        List<Subscription> subscriptions = subscriptionRepository.findAllBySubscriber_UserId(subscriberId);
        return subscriptions.stream()
                .map(Subscription::getAuthor)
                .collect(Collectors.toList());
    }
}
