package org.aper.web.domain.paragraph.dto;

import java.util.List;

public class BatchPayload {
    private List<ParagraphRequestDto.ParagraphDto> modified;
    private List<ParagraphRequestDto.ParagraphDto> added;
    private List<String> deleted;
}
