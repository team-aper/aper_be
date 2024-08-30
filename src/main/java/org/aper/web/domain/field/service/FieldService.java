package org.aper.web.domain.field.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.field.dto.FieldResponseDto.*;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.story.repository.StoryRepository;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldService {

    private final EpisodeRepository episodeRepository;
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final FieldMapper fieldMapper;
    private final FieldHelper fieldHelper;

    public FieldHeaderResponseDto getAuthorInfo(Long authorId) {
        User user = userRepository.findById(authorId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        return new FieldHeaderResponseDto(user.getPenName(), user.getFieldImage(), user.getDescription());
    }

    public HomeResponseDto getFieldHomeData(UserDetailsImpl userDetails, Long authorId) {
        boolean isMyField = fieldHelper.isOwnField(authorId, userDetails);
        List<Episode> episodeList = isMyField
                ? episodeRepository.findAllByEpisode(authorId)
                : episodeRepository.findAllByEpisodeOnlyPublished(authorId);

        List<HomeDetailsResponseDto> detailsList = fieldMapper.toHomeDetailsResponseDtoList(episodeList);
        return new HomeResponseDto(isMyField, detailsList);
    }

    public StoriesResponseDto getStoriesData(UserDetailsImpl userDetails, Long authorId) {
        boolean isMyField = fieldHelper.isOwnField(authorId, userDetails);
        List<Story> storyList = isMyField
                ? storyRepository.findAllByStories(authorId)
                : storyRepository.findAllByStoriesOnlyPublished(authorId);

        List<StoriesDetailsResponseDto> storiesList = fieldMapper.toStoriesDetailsResponseDtoList(storyList);
        return new StoriesResponseDto(isMyField, storiesList);
    }

    public DetailsResponseDto getDetailsData(Long authorId) {
        User user = userRepository.findById(authorId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        return fieldMapper.toDetailsResponseDto(user);
    }
}
