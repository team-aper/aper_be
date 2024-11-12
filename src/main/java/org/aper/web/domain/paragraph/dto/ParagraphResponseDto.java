package org.aper.web.domain.paragraph.dto;

public class ParagraphResponseDto {

    public record Paragraphs(
            String id,
            String content,
            String textAlign,
            String prev,
            String next
    ){}
}
