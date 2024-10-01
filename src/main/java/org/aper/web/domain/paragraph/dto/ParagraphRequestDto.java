package org.aper.web.domain.paragraph.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ParagraphRequestDto {

    public record ParagraphDto(
            @NotBlank(message = "UUID is required.")
            String uuid,

            @NotBlank(message = "Content is required.")
            String content,

            @NotBlank(message = "Previous Uuid is required")
            String previousUuid,

            @NotBlank(message = "Next Uuid is required")
            String nextUuid
    ) {}

    public record BatchPayload(
            List<ParagraphDto> modified,
            List<ParagraphDto> added,
            List<String> deleted
    ) {}
}
