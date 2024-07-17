package org.example.springaper.domain.payment.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.example.springaper.domain.payment.dto.PreOrderRequestDto;
import org.example.springaper.domain.payment.dto.PreOrderResponseDto;
import org.example.springaper.domain.payment.service.PaymentService;
import org.example.springaper.domain.payment.service.RefundsService;
import org.example.springaper.global.security.UserDetailsImpl;
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
    public ResponseEntity<PreOrderResponseDto> prepareOrder(
            @RequestBody @Valid PreOrderRequestDto preOrderRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IamportResponseException, IOException {
        PreOrderResponseDto responseDto = paymentService.prepareOrder(preOrderRequestDto, userDetails.getUser());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/post/{impUid}")
    public ResponseEntity<Void> postOrder(
            @PathVariable String impUid,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IamportResponseException, IOException {
        paymentService.postOrder(impUid, userDetails.getUser());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/refunds/{ordersId}")
    public ResponseEntity<Void> refundsOrder(
            @PathVariable Long ordersId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        refundsService.refundOrder(ordersId, userDetails.getUser());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
