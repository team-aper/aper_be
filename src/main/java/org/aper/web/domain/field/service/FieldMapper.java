package org.aper.web.domain.field.service;

import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.field.dto.FieldResponseDto.DetailsResponseDto;
import org.aper.web.domain.field.dto.FieldResponseDto.HomeDetailsResponseDto;
import org.aper.web.domain.field.dto.FieldResponseDto.StoriesDetailsResponseDto;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.user.dto.UserResponseDto.*;
import org.aper.web.domain.user.entity.User;
import org.aper.web.domain.user.entity.UserHistory;
import org.aper.web.domain.user.entity.constant.HistoryTypeEnum;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FieldMapper {

    public HomeDetailsResponseDto toHomeDetailsResponseDto(Episode episode) {

        LocalDateTime date = episode.isOnDisplay() ? episode.getPublicDate() : episode.getCreatedAt();
        String text = truncateDescription(episode.getDescription());

        return new HomeDetailsResponseDto(
                episode.getStory().getId(),
                episode.getStory().getTitle(),
                episode.getId(),
                episode.getTitle(),
                episode.getChapter(),
                episode.getStory().getGenre().name(),
                date,
                text,
                episode.isOnDisplay()
        );
    }

    public StoriesDetailsResponseDto toStoriesDetailsResponseDto(Story story) {

        LocalDateTime date = story.isOnDisplay() ? story.getPublicDate() : story.getCreatedAt();

        return new StoriesDetailsResponseDto(
                story.getId(),
                story.getTitle(),
                story.getRoutine().name(),
                story.getGenre().name(),
                date,
                story.isOnDisplay()
        );
    }

    public DetailsResponseDto toDetailsResponseDto(User user) {
        return new DetailsResponseDto(
                user.getPenName(),
                user.getEmail()
        );
    }

    public List<HomeDetailsResponseDto> toHomeDetailsResponseDtoList(List<Episode> episodes) {
        return episodes.stream()
                .map(this::toHomeDetailsResponseDto)
                .collect(Collectors.toList());
    }

    public List<StoriesDetailsResponseDto> toStoriesDetailsResponseDtoList(List<Story> stories) {
        return stories.stream()
                .map(this::toStoriesDetailsResponseDto)
                .collect(Collectors.toList());
    }

    private String truncateDescription(String description) {
        if (description == null) {
            return null;
        }

        return description.length() > 250 ? description.substring(0, 250) + "..." : description;
    }

    public HistoryResponseDto userHistoryToDto(List<UserHistory> userHistoryList) {
        List<HistoryDetailResponseDto> educationList = new ArrayList<>();
        List<HistoryDetailResponseDto> awardList = new ArrayList<>();
        List<HistoryDetailResponseDto> publicationList = new ArrayList<>();

        userHistoryList.forEach(userHistory -> {
            HistoryDetailResponseDto responseDto = new HistoryDetailResponseDto(
                    userHistory.getId(),
                    userHistory.getHistoryType().name(),
                    userHistory.getDate(),
                    userHistory.getEndDate(),
                    userHistory.getDescription()
            );

            if (userHistory.getHistoryType() == HistoryTypeEnum.EDUCATION) {
                educationList.add(responseDto);
            }
            if (userHistory.getHistoryType() == HistoryTypeEnum.AWARD) {
                awardList.add(responseDto);
            }
            if (userHistory.getHistoryType() == HistoryTypeEnum.PUBLICATION) {
                publicationList.add(responseDto);
            }
        });

        return new HistoryResponseDto(
                educationList,
                awardList,
                publicationList
        );
    }

    public ClassDescriptionResponseDto classDescriptionToDto(User user) {
        return new ClassDescriptionResponseDto(user.getClassDescription());
    }
}
