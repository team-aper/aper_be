package org.aper.web.domain.main.service;

import com.aperlibrary.curation.entity.Curation;
import com.aperlibrary.episode.entity.Episode;
import com.aperlibrary.user.entity.User;
import org.aper.web.domain.main.dto.MainResponseDto.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MainMapper {

    public static GetEpisodesResponseDto toGetEpisodesResponseDto(Episode episode) {
        return new GetEpisodesResponseDto(
                episode.getId(),
                episode.getDescription(),
                episode.getPublicDate(),
                episode.getStory().getId(),
                episode.getStory().getTitle(),
                episode.getStory().getGenre().name(),
                episode.getStory().getLineStyle().name(),
                episode.getStory().getUser().getUserId(),
                episode.getStory().getUser().getPenName(),
                episode.getStory().getUser().getFieldImage()
        );
    }

    public static GetUsersResponseDto toGetUsersResponseDto(User user) {
        return new GetUsersResponseDto(
                user.getUserId(),
                user.getPenName(),
                user.getFieldImage(),
                user.getDescription()
        );
    }

    public static GetCurationsResponseDto toGetCurationsResponseDto(Curation curation) {
        return new GetCurationsResponseDto(
                curation.getId(),
                curation.getContent(),
                curation.getEpisode().getId(),
                curation.getEpisode().getPublicDate(),
                curation.getEpisode().getStory().getId(),
                curation.getEpisode().getStory().getGenre().name(),
                curation.getEpisode().getStory().getLineStyle().name(),
                curation.getEpisode().getStory().getTitle(),
                curation.getEpisode().getStory().getUser().getPenName()
        );
    }

    public static GetEpisodesListResponseDto toGetEpisodesListResponseDto(List<Episode> episodes) {
        List<GetEpisodesResponseDto> episodeDtos = episodes.stream()
                .map(MainMapper::toGetEpisodesResponseDto)
                .collect(Collectors.toList());
        return new GetEpisodesListResponseDto(episodeDtos);
    }

    public static GetUsersListResponseDto toGetUsersListResponseDto(List<User> users) {
        List<GetUsersResponseDto> userDtos = users.stream()
                .map(MainMapper::toGetUsersResponseDto)
                .collect(Collectors.toList());
        return new GetUsersListResponseDto(userDtos);
    }

    public static GetCurationsListResponseDto toGetCurationsListResponseDto(List<Curation> curations) {
        List<GetCurationsResponseDto> curationDtos = curations.stream()
                .map(MainMapper::toGetCurationsResponseDto)
                .collect(Collectors.toList());
        return new GetCurationsListResponseDto(curationDtos);
    }
}
