package org.aper.web.domain.payment.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aper.web.domain.payment.dto.PreOrderRequestDto;
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
public class PaymentController {
    private final PaymentService paymentService;
    private final RefundsService refundsService;
    @PostMapping("/pre")
    public ResponseDto<PreOrderResponseDto> prepareOrder(
            @RequestBody @Valid PreOrderRequestDto preOrderRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IamportResponseException, IOException {
        PreOrderResponseDto responseDto = paymentService.prepareOrder(preOrderRequestDto, userDetails.user());
        return ResponseDto.success("사전 결제 등록 성공", responseDto);
    }

    @PutMapping("/post/{impUid}")
    public ResponseDto<Void> postOrder(
            @PathVariable String impUid,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IamportResponseException, IOException {
        paymentService.postOrder(impUid, userDetails.user());
        return ResponseDto.success("결제 완료", null);
    }
    @PutMapping("/refunds/{ordersId}")
    public ResponseDto<Void> refundsOrder(
            @PathVariable Long ordersId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IamportResponseException, IOException {
        refundsService.refundsOrder(ordersId, userDetails.user());
        return ResponseDto.success("결제 취소 완료", null);
    }
}
