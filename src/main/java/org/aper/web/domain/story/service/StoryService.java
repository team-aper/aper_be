package org.aper.web.domain.story.service;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.episode.entity.Episode;
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
}
