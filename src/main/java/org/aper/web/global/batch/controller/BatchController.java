package org.aper.web.global.batch.controller;

import lombok.RequiredArgsConstructor;
import org.aper.web.domain.paragraph.dto.ParagraphRequestDto.ItemPayload;
import org.aper.web.domain.user.batch.UserBatchService;
import org.aper.web.global.batch.dto.BatchRequestDto.BatchRequest;
import org.aper.web.global.batch.service.BatchService;
import org.aper.web.global.docs.BatchControllerDocs;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class BatchController implements BatchControllerDocs {

    private final BatchService<ItemPayload> paragraphBatchService;
    private final UserBatchService userBatchService;

    @PostMapping("/paragraph")
    public ResponseDto<Void> processParagraphBatch(@RequestBody BatchRequest<ItemPayload> request,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        paragraphBatchService.processBatch(request, userDetails);
        return ResponseDto.success("문단 배치 요청에 성공하였습니다.");
    }

    @PutMapping("/user")
    public <T> ResponseDto<Void> processUserInfoBatch(@RequestBody BatchRequest<T> request,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println(request);
        userBatchService.processBatch(request, userDetails);
        return ResponseDto.success("유저 정보 배치 요청에 성공하였습니다.");
    }
}
