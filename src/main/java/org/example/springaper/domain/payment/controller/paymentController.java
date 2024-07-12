//package org.example.springaper.domain.payment.controller;
//
//import com.siot.IamportRestClient.IamportClient;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.springaper.domain.payment.dto.PreOrderRequestDto;
//import org.example.springaper.global.security.UserDetailsImpl;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/payment")
//@Slf4j(topic = "결제 컨트롤러")
//public class paymentController {
//    private final IamportClient iamportClient;
//    private final paymentService paymentService;
//
//    @PostMapping("/pre")
//    public ResponseEntity<Void> createPreOrder(
//            @RequestBody @Valid PreOrderRequestDto preOrderRequestDto,
//            @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        paymentService.createPreOrder(preOrderRequestDto, userDetails.getUser());
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//}
