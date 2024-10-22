package org.aper.web.domain.subscription.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.story.repository.StoryRepository;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.AuthorRecommendation;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.SubscribedAuthor;
import org.aper.web.domain.subscription.entity.Subscription;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionHelper {

    private final UserRepository userRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final StoryRepository storyRepository;
    private final RedisTemplate<String, Boolean> redisTemplate;

    @Value("${redis.read.key.prefix}")
    private String readKeyPrefix;

    public User getSubscriber(UserDetailsImpl userDetails) {
        return userRepository.findById(userDetails.user().getUserId())
                .orElseThrow(() -> new ServiceException(ErrorCode.SUBSCRIBER_NOT_FOUND));
    }

    public User getAuthor(Long authorId) {
        return userRepository.findById(authorId)
                .orElseThrow(() -> new ServiceException(ErrorCode.AUTHOR_NOT_FOUND));
    }

    public List<SubscribedAuthor> getSubscribedAuthors(List<Subscription> subscriptions, Long subscriberId) {
        return subscriptions.stream()
                .map(subscription -> {
                    User author = subscription.getAuthor();
                    List<Story> stories = storyRepository.findAllPublicStoriesWithEpisodesByAuthorId(author.getUserId());
                    if (stories.isEmpty()) {
                        throw new ServiceException(ErrorCode.PUBLISHING_NOT_FOUND);
                    }

                    Story latestStory = stories.get(0);
                    List<Episode> publicEpisodes = latestStory.getEpisodeList().stream()
                            .filter(Episode::isOnDisplay)
                            .toList();
                    if (publicEpisodes.isEmpty()) {
                        throw new ServiceException(ErrorCode.PUBLISHING_NOT_FOUND);
                    }

                    Episode latestEpisode = publicEpisodes.get(0);
                    boolean isRead = isEpisodeRead(subscriberId, latestEpisode.getId());

                    return subscriptionMapper.toSubscribedAuthor(author, latestStory, latestEpisode, isRead);
                })
                .collect(Collectors.toList());
    }

    public List<AuthorRecommendation> getTopAuthorsByGenre(StoryGenreEnum genre, Long userId) {
        List<User> topAuthors = userRepository.findTop4ByGenreOrderBySubscriberCountDesc(genre, userId);
        return subscriptionMapper.toAuthorRecommendations(topAuthors, genre);
    }

    private boolean isEpisodeRead(Long userId, Long episodeId) {
        String key = readKeyPrefix + userId + ":" + episodeId;
        return Boolean.TRUE.equals(redisTemplate.opsForValue().get(key));
    }

}
