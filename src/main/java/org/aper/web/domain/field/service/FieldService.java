package org.aper.web.domain.field.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.field.dto.DetailsResponseDto;
import org.aper.web.domain.field.dto.HomeDetailsResponseDto;
import org.aper.web.domain.field.dto.HomeResponseDto;
import org.aper.web.domain.field.dto.StoriesResponseDto;
import org.aper.web.domain.story.repository.StoryRepository;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.repository.UserRepository;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldService {
    private final EpisodeRepository episodeRepository;
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;

    public HomeResponseDto getFieldHomeData(UserDetailsImpl userDetails, Long authorId) {
        HomeResponseDto responseDto;
        List<Episode> episodeList;
        boolean isMyField = isOwnFiled(authorId, userDetails);
        if(isMyField) {
            episodeList = episodeRepository.findAllByEpisode(authorId);
        } else {
            episodeList = episodeRepository.findAllByEpisodeOnlyPublished(authorId);
        }
        responseDto = new HomeResponseDto(isMyField, episodeToDto(episodeList));
        return responseDto;
    }

    public StoriesResponseDto getStoriesData(UserDetailsImpl userDetails, Long authorId) {
        return null;
    }

    public DetailsResponseDto getDetailsData(UserDetailsImpl userDetails, Long authorId) {
        return null;
    }

    private boolean isOwnFiled(Long authorId, UserDetailsImpl userDetails) {
        if(userDetails.equals(null)) {
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
}
