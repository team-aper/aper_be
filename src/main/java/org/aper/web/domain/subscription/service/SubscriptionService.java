package org.aper.web.domain.subscription.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.*;
import org.aper.web.domain.subscription.entity.Subscription;
import org.aper.web.domain.subscription.repository.SubscriptionRepository;
import org.aper.web.domain.user.entity.User;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionHelper subscriptionHelper;

    @Transactional
    public void subscribe(UserDetailsImpl userDetails, Long authorId) {
        User subscriber = subscriptionHelper.getSubscriber(userDetails);
        User author = subscriptionHelper.getAuthor(authorId);
        subscriptionRepository.save(Subscription.builder().subscriber(subscriber).author(author).build());
    }

    @Transactional(readOnly = true)
    public IsSubscribed isSubscribed(UserDetailsImpl userDetails, Long authorId) {
        User subscriber = subscriptionHelper.getSubscriber(userDetails);
        User author = subscriptionHelper.getAuthor(authorId);
        boolean isSubscribed = subscriptionRepository.existsBySubscriberAndAuthor(subscriber, author);
        return new IsSubscribed(isSubscribed);
    }

    @Transactional(readOnly = true)
    public IsSubscriber isSubscriber(UserDetailsImpl userDetails) {
        Long userId = userDetails.user().getUserId();
        boolean isSubscriber = subscriptionRepository.existsBySubscriber_UserId(userId);
        return new IsSubscriber(isSubscriber);
    }

    @Transactional(readOnly = true)
    public SubscribedAuthors getSubscribedAuthors(UserDetailsImpl userDetails, int page, int size) {
        Long subscriberId = userDetails.user().getUserId();
        Pageable pageable = PageRequest.of(page, size);
        Page<Subscription> subscriptionsPage = subscriptionRepository.findAllBySubscriber_UserId(subscriberId, pageable);
        List<SubscribedAuthor> subscribedAuthors = subscriptionHelper.getSubscribedAuthors(subscriptionsPage.getContent(), subscriberId);
        return new SubscribedAuthors(subscribedAuthors);
    }

    @Transactional(readOnly = true)
    public AuthorRecommendations getRecommendedAuthors(UserDetailsImpl userDetails, int page, int size) {
        Long userId = userDetails.user().getUserId();
        List<AuthorRecommendation> dailyRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.DAILY, userId, page, size);
        List<AuthorRecommendation> romanceRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.ROMANCE, userId, page, size);
        List<AuthorRecommendation> horrorRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.HORROR, userId, page, size);
        List<AuthorRecommendation> sfRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.SF, userId, page, size);
        List<AuthorRecommendation> queerRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.QUEER, userId, page, size);
        List<AuthorRecommendation> societyRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.SOCIETY, userId, page, size);
        List<AuthorRecommendation> artRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.ART, userId, page, size);
        List<AuthorRecommendation> criticismRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.CRITICISM, userId, page, size);
        List<AuthorRecommendation> poetryRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.POETRY, userId, page, size);

        return new AuthorRecommendations(
                dailyRecommendations,
                romanceRecommendations,
                horrorRecommendations,
                sfRecommendations,
                queerRecommendations,
                societyRecommendations,
                artRecommendations,
                criticismRecommendations,
                poetryRecommendations
        );
    }

    @Transactional
    public void unsubscribe(UserDetailsImpl userDetails, Long authorId) {
        User subscriber = subscriptionHelper.getSubscriber(userDetails);
        User author = subscriptionHelper.getAuthor(authorId);
        Subscription subscription = subscriptionRepository.findBySubscriberAndAuthor(subscriber, author)
                .orElseThrow(() -> new ServiceException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
        subscriptionRepository.delete(subscription);
    }
}
