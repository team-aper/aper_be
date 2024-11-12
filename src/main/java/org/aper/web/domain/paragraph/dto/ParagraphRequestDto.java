package org.aper.web.domain.paragraph.dto;

import jakarta.validation.constraints.NotBlank;

public class ParagraphRequestDto {

    public record ItemPayload(
            @NotBlank(message = "Content is required.")
            String content,

            @NotBlank(message = "UUID is required.")
            String id,

            String textAlign,

            String prev,

            String next
    ) {}
}
