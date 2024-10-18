package org.aper.web.domain.subscription.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.story.dto.StoryResponseDto.GetRecommendedStory;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.story.repository.StoryRepository;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.AuthorRecommendations;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.AuthorRecommendation;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.IsSubscribed;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.SubscribedAuthor;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.SubscribedAuthors;
import org.aper.web.domain.subscription.entity.Subscription;
import org.aper.web.domain.subscription.repository.SubscriptionRepository;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final StoryRepository storyRepository;
    private final RedisTemplate<String, Boolean> redisTemplate;

    private static final String READ_KEY_PREFIX = "episode:read:";

    @Transactional
    public void subscribe(UserDetailsImpl userDetails, Long authorId) {
        User subscriber = userRepository.findById(userDetails.user().getUserId())
                .orElseThrow(() -> new ServiceException(ErrorCode.SUBSCRIBER_NOT_FOUND));

        User author = userRepository.findById(authorId).orElseThrow(() -> new ServiceException(ErrorCode.AUTHOR_NOT_FOUND));

        subscriptionRepository.save(Subscription.builder().subscriber(subscriber).author(author).build());
    }

    @Transactional(readOnly = true)
    public IsSubscribed isSubscribed(UserDetailsImpl userDetails, Long authorId) {
        User subscriber = userRepository.findById(userDetails.user().getUserId())
                .orElseThrow(() -> new ServiceException(ErrorCode.SUBSCRIBER_NOT_FOUND));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ServiceException(ErrorCode.AUTHOR_NOT_FOUND));

        boolean isSubscribed = subscriptionRepository.existsBySubscriberAndAuthor(subscriber, author);
        return new IsSubscribed(isSubscribed);
    }

    @Transactional(readOnly = true)
    public SubscribedAuthors getSubscribedAuthors(UserDetailsImpl userDetails) {
        User subscriber = userRepository.findById(userDetails.user().getUserId())
                .orElseThrow(() -> new ServiceException(ErrorCode.SUBSCRIBER_NOT_FOUND));

        List<Subscription> subscriptions = subscriptionRepository.findAllByAuthor_UserId(subscriber.getUserId());

        List<SubscribedAuthor> subscribedAuthors = subscriptions.stream()
                .map(subscription -> {
                    User author = subscription.getAuthor();

                    List<Story> stories = storyRepository.findTop3ByUser_UserIdOrderByPublicDateDesc(author.getUserId());

                    Story latestStory = stories.get(0);
                    Episode latestEpisode = latestStory.getEpisodeList().get(0);
                    boolean isRead = isEpisodeRead(subscriber.getUserId(), latestEpisode.getId());

                    return new SubscribedAuthor(
                            author.getPenName(),
                            author.getFieldImage(),
                            author.getUserId(),
                            latestEpisode.getChapter(),
                            latestStory.getId(),
                            latestStory.getTitle(),
                            latestStory.getGenre().toString(),
                            latestEpisode.getPublicDate().toLocalDate(),
                            latestEpisode.getId().toString(),
                            latestEpisode.getTitle(),
                            latestEpisode.getDescription(),
                            isRead
                    );
                })
                .collect(Collectors.toList());

        return new SubscribedAuthors(subscribedAuthors);
    }

    @Transactional(readOnly = true)
    public AuthorRecommendations getRecommendedAuthors() {
        List<AuthorRecommendation> dailyRecommendations = getTopAuthorsByGenre(StoryGenreEnum.DAILY);
        List<AuthorRecommendation> romanceRecommendations = getTopAuthorsByGenre(StoryGenreEnum.ROMANCE);
        List<AuthorRecommendation> horrorRecommendations = getTopAuthorsByGenre(StoryGenreEnum.HORROR);
        List<AuthorRecommendation> sfRecommendations = getTopAuthorsByGenre(StoryGenreEnum.SF);
        List<AuthorRecommendation> queerRecommendations = getTopAuthorsByGenre(StoryGenreEnum.QUEER);
        List<AuthorRecommendation> societyRecommendations = getTopAuthorsByGenre(StoryGenreEnum.SOCIETY);
        List<AuthorRecommendation> artRecommendations = getTopAuthorsByGenre(StoryGenreEnum.ART);
        List<AuthorRecommendation> criticismRecommendations = getTopAuthorsByGenre(StoryGenreEnum.CRITICISM);
        List<AuthorRecommendation> poetryRecommendations = getTopAuthorsByGenre(StoryGenreEnum.POETRY);

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

    private List<AuthorRecommendation> getTopAuthorsByGenre(StoryGenreEnum genre) {
        List<User> topAuthors = userRepository.findTop4ByGenreOrderBySubscriberCountDesc(genre);

        return topAuthors.stream()
                .map(author -> new AuthorRecommendation(
                        author.getPenName(),
                        author.getFieldImage(),
                        author.getDescription(),
                        author.getUserId(),
                        author.getStoryList().stream()
                                .limit(3)
                                .map(story -> new GetRecommendedStory(
                                        story.getId(),
                                        story.getTitle()
                                ))
                                .collect(Collectors.toList()),
                        subscriptionRepository.countByAuthorId(author.getUserId())
                ))
                .collect(Collectors.toList());
    }

    private boolean isEpisodeRead(Long userId, Long episodeId) {
        String key = READ_KEY_PREFIX + userId + ":" + episodeId;
        return Boolean.TRUE.equals(redisTemplate.opsForValue().get(key));
    }
}