package org.aper.web.domain.subscription.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.*;
import org.aper.web.domain.subscription.entity.Subscription;
import org.aper.web.domain.subscription.repository.SubscriptionRepository;
import org.aper.web.domain.user.entity.User;
import org.aper.web.global.security.UserDetailsImpl;
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
    public SubscribedAuthors getSubscribedAuthors(UserDetailsImpl userDetails) {
        Long subscriberId = userDetails.user().getUserId();
        List<Subscription> subscriptions = subscriptionRepository.findAllBySubscriber_UserId(subscriberId);
        List<SubscribedAuthor> subscribedAuthors = subscriptionHelper.getSubscribedAuthors(subscriptions, subscriberId);
        return new SubscribedAuthors(subscribedAuthors);
    }

    @Transactional(readOnly = true)
    public AuthorRecommendations getRecommendedAuthors(UserDetailsImpl userDetails) {
        Long userId = userDetails.user().getUserId();
        List<AuthorRecommendation> dailyRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.DAILY, userId);
        List<AuthorRecommendation> romanceRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.ROMANCE, userId);
        List<AuthorRecommendation> horrorRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.HORROR, userId);
        List<AuthorRecommendation> sfRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.SF, userId);
        List<AuthorRecommendation> queerRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.QUEER, userId);
        List<AuthorRecommendation> societyRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.SOCIETY, userId);
        List<AuthorRecommendation> artRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.ART, userId);
        List<AuthorRecommendation> criticismRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.CRITICISM, userId);
        List<AuthorRecommendation> poetryRecommendations = subscriptionHelper.getTopAuthorsByGenre(StoryGenreEnum.POETRY, userId);

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
}
