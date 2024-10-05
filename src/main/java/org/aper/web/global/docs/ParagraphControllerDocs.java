package org.aper.web.global.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto.BatchRequest;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Paragraph Batch", description = "문단 자동 저장을 위한 배치 처리 API")
public interface ParagraphControllerDocs {

    @Operation(summary = "문단 'POST', 'PUT', 'DELETE' 요청에 대한 batch 처리",
               description = "문단 자동 저장 기능을 위한 배치 요청을 처리하는 API입니다.")
    ResponseDto<Void> processBatch(
            @RequestBody BatchRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails);

}
