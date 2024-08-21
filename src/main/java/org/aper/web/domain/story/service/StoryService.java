package org.aper.web.domain.story.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.story.constant.StoryGenreEnum;
import org.aper.web.domain.story.constant.StoryLineStyleEnum;
import org.aper.web.domain.story.constant.StoryRoutineEnum;
import org.aper.web.domain.story.dto.StoryRequestDto.*;
import org.aper.web.domain.story.entity.Story;
import org.aper.web.domain.story.repository.StoryRepository;
import org.aper.web.domain.user.entity.User;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoryService {
    private final StoryRepository storyRepository;

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
}
