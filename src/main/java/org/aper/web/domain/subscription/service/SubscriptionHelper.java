package org.aper.web.domain.subscription.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.story.entity.constant.StoryGenreEnum;
import org.aper.web.domain.story.repository.StoryRepository;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.AuthorRecommendation;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.SubscribedAuthor;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionHelper {

    private final UserRepository userRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final StoryRepository storyRepository;
    private final RedisTemplate<String, Boolean> redisTemplate;
    private final EpisodeRepository episodeRepository;

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

    @Transactional(readOnly = true)
    public List<SubscribedAuthor> getSubscribedAuthorsSinceSubscription(Long subscriberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Episode> latestEpisodesPage = episodeRepository.findLatestEpisodesBySubscriberSinceSubscription(subscriberId, pageable);

        return latestEpisodesPage.getContent().stream()
                .map(latestEpisode -> {
                    User author = latestEpisode.getStory().getUser();
                    boolean isRead = isEpisodeRead(subscriberId, latestEpisode.getId());

                    return subscriptionMapper.toSubscribedAuthor(
                            author,
                            latestEpisode.getStory(),
                            latestEpisode,
                            isRead
                    );
                })
                .collect(Collectors.toList());
    }

    public List<AuthorRecommendation> getTopAuthorsByGenre(StoryGenreEnum genre, Long userId) {
        List<User> topAuthors = userRepository.findTop4ByGenreOrderBySubscriberCountDesc(genre, userId);
        return subscriptionMapper.toAuthorRecommendations(topAuthors, genre);
    }

    private boolean isEpisodeRead(Long userId, Long episodeId) {
        String key = readKeyPrefix + ":" + userId + ":" + episodeId;
        return Boolean.TRUE.equals(redisTemplate.opsForValue().get(key));
    }

}
