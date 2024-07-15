package org.example.springaper.domain.payment.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springaper.domain.payment.dto.PreOrderRequestDto;
import org.example.springaper.domain.payment.service.PaymentService;
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

    @PostMapping("/pre")
    public ResponseEntity<Void> prepareOrder(
            @RequestBody @Valid PreOrderRequestDto preOrderRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IamportResponseException, IOException {
        paymentService.prepareOrder(preOrderRequestDto, userDetails.getUser());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/post/")
    public ResponseEntity<Void> postOrder(
            @PathVariable String impUid,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IamportResponseException, IOException {
        paymentService.postOrder(impUid, userDetails.getUser());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
