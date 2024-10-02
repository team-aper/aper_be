package org.aper.web.domain.paragraph.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ParagraphRequestDto {

    public record ItemPayload(
            @NotBlank(message = "UUID is required.")
            String content,

            @NotBlank(message = "Content is required.")
            String id,

            String prev,

            String next
    ) {}

    public record BatchRequest (
            List<BatchOperation> batch
    ) {}

    public record BatchOperation (
            String method,
            String url,
            List<ItemPayload> body
    ) {}
}
