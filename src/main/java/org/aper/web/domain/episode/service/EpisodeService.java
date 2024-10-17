package org.aper.web.domain.episode.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.dto.EpisodeRequestDto.DeleteEpisodeDto;
import org.aper.web.domain.episode.dto.EpisodeRequestDto.TitleChangeDto;
import org.aper.web.domain.episode.dto.EpisodeResponseDto.EpisodeHeaderDto;
import org.aper.web.domain.episode.dto.EpisodeResponseDto.EpisodeTextDto;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.kafka.service.KafkaEpisodesProducerService;
import org.aper.web.domain.paragraph.dto.ParagraphResponseDto.Paragraphs;
import org.aper.web.domain.story.service.StoryHelper;
import org.aper.web.domain.subscription.entity.Subscription;
import org.aper.web.domain.subscription.repository.SubscriptionRepository;
import org.aper.web.domain.subscription.service.RedisPublisher;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final EpisodeHelper episodeHelper;
    private final EpisodeMapper episodeMapper;
    private final StoryHelper storyHelper;
    private final KafkaEpisodesProducerService producerService;
    private final RedisPublisher redisPublisher;
    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public void changePublicStatus(Long episodeId, UserDetailsImpl userDetails) {
        Episode episode = episodeHelper.validateEpisodeExists(episodeId);
        episodeHelper.validateUserIsAuthor(episode, userDetails);

        episode.updateOnDisplay();
        episodeRepository.save(episode);

        producerService.sendUpdate(episode);

        List<Subscription> subscribers = subscriptionRepository.findAllByAuthor_UserId(userDetails.user().getUserId());
        for (Subscription subscription : subscribers) {
            Long subscriberId = subscription.getSubscriber().getUserId();
            redisPublisher.publish(subscriberId, true);
        }
    }

    @Transactional
    public void changeTitle(UserDetailsImpl userDetails, Long episodeId, TitleChangeDto titleChangeDto) {
        Episode episode = episodeHelper.validateEpisodeExists(episodeId);
        episodeHelper.validateUserIsAuthor(episode, userDetails);

        episode.updateTitle(titleChangeDto.title());

        episodeRepository.save(episode);

        producerService.sendUpdate(episode);
    }

    @Transactional
    public void deleteEpisode(UserDetailsImpl userDetails, Long episodeId, DeleteEpisodeDto episodeDto) {
        Episode episode = episodeHelper.validateEpisodeExists(episodeId);
        episodeHelper.validateUserIsAuthor(episode, userDetails);
        episodeRepository.delete(episode);
        episodeRepository.flush();
        episodeRepository.decrementChaptersAfterDeletion(episodeDto.storyId(), episodeDto.chapter());

        producerService.sendDelete(episodeId);
    }

    public EpisodeHeaderDto getEpisodeHeader(UserDetailsImpl userDetails, Long episodeId) {
        Episode episode = episodeHelper.validateEpisodeExists(episodeId);
        if (storyHelper.isOwnStory(episode.getStory().getId(), userDetails)){
            return episodeMapper.toEpisodeHeaderDto(episode);
        }

        boolean isRead = false;

        if (userDetails != null) {
            Long userId = userDetails.user().getUserId();
            isRead = episodeHelper.isEpisodeRead(userId, episodeId);

            if (!isRead) {
                episodeHelper.markEpisodeAsRead(userId, episodeId);
            }
        }

        if (!episode.isOnDisplay()){
            throw new ServiceException(ErrorCode.EPISODE_NOT_PUBLISHED);
        }

        return episodeMapper.toEpisodeHeaderDto(episode);
    }

    public EpisodeTextDto getEpisodeText(UserDetailsImpl userDetails, Long episodeId) {
        Episode episode = episodeHelper.validateEpisodeExists(episodeId);

        if (storyHelper.isOwnStory(episode.getStory().getId(), userDetails)){
            return new EpisodeTextDto(episodeHelper.getSortedParagraphs(episode.getParagraphs()));
        }

        if (!episode.isOnDisplay()){
            throw new ServiceException(ErrorCode.EPISODE_NOT_PUBLISHED);
        }

        List<Paragraphs> sortedParagraphs = episodeHelper.getSortedParagraphs(episode.getParagraphs());

        return new EpisodeTextDto(sortedParagraphs);
    }
}
