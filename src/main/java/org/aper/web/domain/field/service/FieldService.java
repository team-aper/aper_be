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


    public FieldHeaderResponseDto getAuthorInfo(Long authorId) {
        User user = userRepository.findById(authorId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        return new FieldHeaderResponseDto(user.getPenName(), user.getFieldImage(), user.getDescription());
    }

    public HomeResponseDto getFieldHomeData(UserDetailsImpl userDetails, Long authorId) {
        List<Episode> episodeList;
        boolean isMyField = isOwnFiled(authorId, userDetails);

        if(isMyField) {
            episodeList = episodeRepository.findAllByEpisode(authorId);
            return new HomeResponseDto(isMyField, episodeToDto(episodeList));
        }

        episodeList = episodeRepository.findAllByEpisodeOnlyPublished(authorId);
        return new HomeResponseDto(isMyField, episodeToDto(episodeList));
    }

    public StoriesResponseDto getStoriesData(UserDetailsImpl userDetails, Long authorId) {
        List<Story> storyList;
        boolean isMyField = isOwnFiled(authorId, userDetails);

        if(isMyField) {
            storyList = storyRepository.findAllByStories(authorId);
            return new StoriesResponseDto(isMyField, storiesToDto(storyList));
        }

        storyList = storyRepository.findAllByStoriesOnlyPublished(authorId);
        return new StoriesResponseDto(isMyField, storiesToDto(storyList));
    }

    public DetailsResponseDto getDetailsData(UserDetailsImpl userDetails, Long authorId) {
        User user = userRepository.findById(authorId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        return new DetailsResponseDto(user);
    }

    private boolean isOwnFiled(Long authorId, UserDetailsImpl userDetails) {
        if(userDetails == null) {
            return false;
        }
        User user = userDetails.user();
        Long userId = user.getUserId();
        return userId.equals(authorId);
    }

    private List<HomeDetailsResponseDto> episodeToDto(List<Episode> episodeList) {
        return episodeList.stream()
                .map(HomeDetailsResponseDto::new)
                .toList();
    }

    private List<StoriesDetailsResponseDto> storiesToDto(List<Story> storyList) {
        return storyList.stream()
                .map(StoriesDetailsResponseDto::new)
                .toList();
    }

}
