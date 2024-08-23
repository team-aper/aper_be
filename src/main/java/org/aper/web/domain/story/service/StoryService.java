package org.aper.web.domain.story.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;
import org.aper.web.domain.episode.repository.EpisodeRepository;
import org.aper.web.domain.story.constant.StoryGenreEnum;
import org.aper.web.domain.story.constant.StoryLineStyleEnum;
import org.aper.web.domain.story.constant.StoryRoutineEnum;
import org.aper.web.domain.story.dto.StoryRequestDto.StoryCreateDto;
import org.aper.web.domain.story.dto.StoryResponseDto.GetStoryDto;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.story.repository.StoryRepository;
import org.aper.web.domain.user.entity.User;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryService {
    private final StoryRepository storyRepository;
    private final EpisodeRepository episodeRepository;

    @Transactional
    public void changePublicStatus(Long storyId, UserDetailsImpl userDetails) {
        Story existStory = storyRepository.findByStoryAuthor(storyId).orElseThrow(() ->
                new ServiceException(ErrorCode.STORY_NOT_FOUND)
        );

        User storyAuthor = existStory.getUser();
        User accessUser = userDetails.user();

        if(!storyAuthor.getUserId().equals(accessUser.getUserId())) {
            throw new ServiceException(ErrorCode.NOT_AUTHOR_OF_STORY);
        }

        existStory.updateOnDisplay();
        storyRepository.save(existStory);
    }

    @Transactional
    public void createStory(UserDetailsImpl userDetails, StoryCreateDto storyCreateDto) {

        Story story = Story.builder()
                .title(storyCreateDto.title())
                .routine(StoryRoutineEnum.fromString(storyCreateDto.routine()))
                .genre(StoryGenreEnum.fromString(storyCreateDto.genre()))
                .lineStyle(StoryLineStyleEnum.fromString(storyCreateDto.lineStyle()))
                .user(userDetails.user())
                .build();

        storyRepository.save(story);
    }

    public GetStoryDto getStory(UserDetailsImpl userDetails, Long storyId) {

        boolean isOwned = isOwnStory(storyId, userDetails);

        if (isOwned) {
            Story story = storyRepository.findByStoryAuthor(storyId).orElseThrow(() -> new ServiceException(ErrorCode.STORY_NOT_FOUND));
            List<Episode> episodes = episodeRepository.findAllByEpisode(userDetails.user().getUserId());
            return new GetStoryDto(
                    story.getTitle(),
                    story.getRoutine().name(),
                    story.getUser().getPenName(),
                    story.getGenre().name(),
                    story.getLineStyle().name(),
                    story.getPublicDate(),
                    story.isOnDisplay(),
                    episodes
            );
        }

        Story story = storyRepository.findByStoryAuthor(storyId).orElseThrow(() -> new ServiceException(ErrorCode.STORY_NOT_FOUND));
        List<Episode> episodes = episodeRepository.findAllByEpisodeOnlyPublished(story.getUser().getUserId());
        return new GetStoryDto(
                story.getTitle(),
                story.getRoutine().name(),
                story.getUser().getPenName(),
                story.getGenre().name(),
                story.getLineStyle().name(),
                story.getPublicDate(),
                story.isOnDisplay(),
                episodes
        );
    }

    private boolean isOwnStory(Long storyId, UserDetailsImpl userDetails) {
        if(userDetails == null) {
            return false;
        }
        return storyRepository.existsByIdAndUser_UserId(storyId, userDetails.user().getUserId());
    }

}
