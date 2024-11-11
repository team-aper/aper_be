package org.aper.web.domain.episode.dto;

import org.aper.web.domain.paragraph.dto.ParagraphResponseDto.Paragraphs;

import java.time.LocalDateTime;
import java.util.List;

public class EpisodeResponseDto {

    public record CreatedEpisodeDto(
            Long episodeId,
            String title,
            Long chapter,
            LocalDateTime date,
            String description,
            boolean isPublished,
            String dDay
    ) {
    }

    public record EpisodeHeaderDto(
            Long episodeId,
            Long authorId,
            Long storyId,
            String storyTitle,
            String episodeTitle,
            Long chapter,
            String genre,
            String storyLineStyle,
            LocalDateTime date,
            boolean isPublished
    ){}

    public record EpisodeTextDto(
            List<Paragraphs> text
    ){}
}
