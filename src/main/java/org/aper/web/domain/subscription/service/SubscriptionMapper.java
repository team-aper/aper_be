package org.aper.web.domain.subscription.service;

import com.aperlibrary.episode.entity.Episode;
import com.aperlibrary.story.entity.Story;
import com.aperlibrary.story.entity.constant.StoryGenreEnum;
import com.aperlibrary.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.aper.web.domain.story.dto.StoryResponseDto.GetRecommendedStory;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.AuthorRecommendation;
import org.aper.web.domain.subscription.dto.SubscriptionResponseDto.SubscribedAuthor;
import org.aper.web.domain.subscription.repository.SubscriptionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionMapper {

    private final SubscriptionRepository subscriptionRepository;

    public SubscribedAuthor toSubscribedAuthor(User author, Story latestStory, Episode latestEpisode, boolean isRead) {
        return new SubscribedAuthor(
                author.getPenName(),
                author.getFieldImage(),
                author.getUserId(),
                latestEpisode.getChapter(),
                latestStory.getId(),
                latestStory.getTitle(),
                latestStory.getGenre().toString(),
                latestEpisode.getPublicDate().toLocalDate(),
                latestEpisode.getId(),
                latestEpisode.getTitle(),
                latestEpisode.getDescription(),
                isRead
        );
    }

    public List<AuthorRecommendation> toAuthorRecommendations(List<User> authors, StoryGenreEnum genre) {
        return authors.stream()
                .map(author -> {
                    List<Story> stories = author.getStoryList().stream()
                            .filter(story -> story.getGenre().equals(genre))
                            .limit(3)
                            .toList();
                    Long subscriberCount = subscriptionRepository.countByAuthorId(author.getUserId());
                    return new AuthorRecommendation(
                            author.getPenName(),
                            author.getFieldImage(),
                            author.getDescription(),
                            author.getUserId(),
                            stories.stream().map(story -> new GetRecommendedStory(story.getId(), story.getTitle()))
                                    .collect(Collectors.toList()),
                            subscriberCount
                    );
                })
                .collect(Collectors.toList());
    }
}