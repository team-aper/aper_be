package org.aper.web.domain.story.service;

import com.aperlibrary.story.entity.Story;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.story.repository.StoryRepository;
import org.aper.web.global.handler.ErrorCode;
import org.aper.web.global.handler.exception.ServiceException;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoryHelper {
    private final StoryRepository storyRepository;

    public Story validateStoryOwnership(Long storyId, UserDetailsImpl userDetails) {
        Story story = storyRepository.findByStoryAuthor(storyId).orElseThrow(() ->
                new ServiceException(ErrorCode.STORY_NOT_FOUND)
        );

        if (!story.getUser().getUserId().equals(userDetails.user().getUserId())) {
            throw new ServiceException(ErrorCode.NOT_AUTHOR_OF_STORY);
        }

        return story;
    }

    public Story validateStoryAccessibility(Long storyId) {
        return storyRepository.findByStoryAuthor(storyId).orElseThrow(() ->
                new ServiceException(ErrorCode.STORY_NOT_FOUND)
        );
    }

    public boolean isOwnStory(Long storyId, UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return false;
        }
        return storyRepository.existsByIdAndUser_UserId(storyId, userDetails.user().getUserId());
    }
}
