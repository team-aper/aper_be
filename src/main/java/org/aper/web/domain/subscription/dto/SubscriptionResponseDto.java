package org.aper.web.domain.subscription.dto;

import org.aper.web.domain.story.dto.StoryResponseDto.GetRecommendedStory;

import java.time.LocalDate;
import java.util.List;

public class SubscriptionResponseDto {

    public record IsSubscribed(
            boolean isSubscribed
    ){}

    public record SubscribedAuthors(
            List<SubscribedAuthor> authors
    ){}

    public record IsSubscriber(
            boolean isSubscriber
    ){}

    public record AuthorRecommendations(
            List<AuthorRecommendation> daily,
            List<AuthorRecommendation> romance,
            List<AuthorRecommendation> horror,
            List<AuthorRecommendation> sf,
            List<AuthorRecommendation> queer,
            List<AuthorRecommendation> society,
            List<AuthorRecommendation> art,
            List<AuthorRecommendation> criticism,
            List<AuthorRecommendation> poetry
    ){}

    public record SubscribedAuthor(
            String penName,
            String fieldImage,
            Long authorId,
            Long chapter,
            Long storyId,
            String storyTitle,
            String genre,
            LocalDate publicDate,
            Long episodeId,
            String episodeTitle,
            String description,
            boolean isRead
    ){}

    public record AuthorRecommendation(
            String penName,
            String fieldImage,
            String description,
            Long authorId,
            List<GetRecommendedStory> storyList,
            Long subscribers
    ){}
}
