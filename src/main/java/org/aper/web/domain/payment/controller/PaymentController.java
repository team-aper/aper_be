package org.aper.web.domain.payment.controller;

import com.aperlibrary.payment.dto.PreOrderRequestDto;
import com.siot.IamportRestClient.exception.IamportResponseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.payment.dto.PreOrderResponseDto;
import org.aper.web.domain.payment.service.PaymentService;
import org.aper.web.domain.payment.service.RefundsService;
import org.aper.web.global.dto.ResponseDto;
import org.aper.web.global.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@Slf4j(topic = "결제 컨트롤러")
@Tag(name = "결제 관련 API 모음 (미사용 API)")
public class PaymentController {
    private final PaymentService paymentService;
    private final RefundsService refundsService;
    @PostMapping("/pre")
    @Operation(summary = "사전 결제 API", description = "Import를 통해 실제 결제를 하기전 이 API로 사전 요청을 하면 서버에서 Import에 결제 데이터 사전 통보와 프론트에게 결제 금액, 결제명을 응답함")
    public ResponseDto<PreOrderResponseDto> prepareOrder(
            @RequestBody @Valid PreOrderRequestDto preOrderRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IamportResponseException, IOException {
        PreOrderResponseDto responseDto = paymentService.prepareOrder(preOrderRequestDto, userDetails.user());
        return ResponseDto.success("사전 결제 등록 성공", responseDto);
    }

    @PutMapping("/post/{impUid}")
    @Operation(summary = "결제 성공시 API", description = "프론트에서 Import를 통해 실제 결제에 성공했을 경우 서버에서 결제 관련 데이터 업데이트 함")
    public ResponseDto<Void> postOrder(
            @PathVariable String impUid,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IamportResponseException, IOException {
        paymentService.postOrder(impUid, userDetails.user());
        return ResponseDto.success("결제 완료", null);
    }
    @PutMapping("/refunds/{ordersId}")
    @Operation(summary = "결제 환불 API", description = "이 API를 통해 결제 환불 요청 시 서버에서 Import에 결제 환불 요청을 보내고 관련 데이터 업데이트 함")
    public ResponseDto<Void> refundsOrder(
            @PathVariable Long ordersId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IamportResponseException, IOException {
        refundsService.refundsOrder(ordersId, userDetails.user());
        return ResponseDto.success("결제 취소 완료", null);
    }
}
