package org.example.springaper.domain.payment.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.siot.IamportRestClient.response.Prepare;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springaper.domain.payment.dto.PreOrderRequestDto;
import org.example.springaper.domain.payment.entity.DigitalProduct;
import org.example.springaper.domain.payment.entity.Orders;
import org.example.springaper.domain.payment.entity.OrdersDetail;
import org.example.springaper.domain.payment.entity.PaymentInfo;
import org.example.springaper.domain.payment.repository.DigitalProductRepository;
import org.example.springaper.domain.payment.repository.OrdersDetailRepository;
import org.example.springaper.domain.payment.repository.OrdersRepository;
import org.example.springaper.domain.payment.repository.PaymentInfoRepository;
import org.example.springaper.domain.user.entity.User;
import org.example.springaper.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "결제 서비스")
public class PaymentService {
    private final IamportClient iamportClient;
    private final OrdersRepository ordersRepository;
    private final OrdersDetailRepository ordersDetailRepository;
    private final DigitalProductRepository digitalProductRepository;
    private final PaymentInfoRepository paymentInfoRepository;
    private final UserRepository userRepository;
    public void prepareOrder(PreOrderRequestDto preOrderRequestDto, User user) throws IamportResponseException, IOException {
        PrepareData prepareData = new PrepareData(preOrderRequestDto.getMerchantUid(), preOrderRequestDto.getTotalAmount());
        IamportResponse<Prepare> iamportResponse = iamportClient.postPrepare(prepareData);
        if (iamportResponse.getCode() != 0) {
            throw new IllegalArgumentException(iamportResponse.getMessage());
        }
        log.info("사전 결제 아임포트 추가 성공");
        PaymentInfo prePaymentInfo = new PaymentInfo(preOrderRequestDto);
        paymentInfoRepository.save(prePaymentInfo);
        Orders preOrders = new Orders(preOrderRequestDto.getTotalAmount().longValue(), user, prePaymentInfo);
        ordersRepository.save(preOrders);
        log.info("사전 주문 테이블 생성 성공");
        createOrdersDetail(preOrders, preOrderRequestDto);
    }
    public List<DigitalProduct> getProductList(PreOrderRequestDto requestDto) {
        List<Long> productIds = requestDto.getOrderItems().stream()
                .map(Long::valueOf) // Integer를 Long으로 변환
                .toList();
        return digitalProductRepository.findAllById(productIds);
    }

    public void createOrdersDetail(Orders orders, PreOrderRequestDto preOrderRequestDto) {
        preOrderRequestDto.getOrderItems().stream()
            .forEach(productId -> {
                DigitalProduct digitalProduct = digitalProductRepository.findById(productId.longValue()).orElseThrow(
                        () -> new IllegalArgumentException("해당 상품이 없습니다.")
                );
                OrdersDetail ordersDetail = new OrdersDetail(digitalProduct, orders);
                ordersDetailRepository.save(ordersDetail);
                log.info("주문 디테일 생성 성공");
            });
    }
    @Transactional
    public void postOrder(String impUid, User user) throws IamportResponseException, IOException {
        IamportResponse<Payment> payment = iamportClient.paymentByImpUid(impUid);
        Payment response = payment.getResponse();
        String responseMerchantUid = response.getMerchantUid();
        String responseImpUid = response.getImpUid();
        LocalDateTime responsePaidAt = changePaidAtLocalDateTime(response.getPaidAt());

        PaymentInfo paymentInfo = paymentInfoRepository.findByMerchantUid(responseMerchantUid).orElseThrow(() ->
                new IllegalArgumentException("존재 하지 않는 결제 내용입니다.")
        );

        Orders orders = ordersRepository.findByPaymentInfoPaymentinfoId(paymentInfo.getPaymentinfoId()).orElseThrow(() ->
            new IllegalArgumentException("존재 하지 않는 주문 내용입니다.")
        );

        if (!Objects.equals(orders.getUser().getUserId(), user.getUserId())) {
            throw new IllegalArgumentException("주문한 유저와 결제한 유저가 일치하지 않습니다.");
        }

        List<OrdersDetail> ordersDetailList = ordersDetailRepository.findAllByOrdersOrdersId(orders.getOrdersId());

        paymentInfo.updateImpUid(responseImpUid);
        paymentInfo.updatePaymentDate(responsePaidAt);

        AtomicReference<Long> point = new AtomicReference<>(0L);
        ordersDetailList.stream()
            .forEach(ordersDetail -> {
                ordersDetail.updatePaymentDate(responsePaidAt);
                ordersDetail.updatePaymentStatusPaid();
                Long productId = ordersDetail.getDigitalProduct().getProductId();
                DigitalProduct product = digitalProductRepository.findById(productId).orElseThrow(() ->
                        new IllegalArgumentException("존재 하지 상품입니다.")
                );
                point.updateAndGet(v -> v + product.getValue());
            });

        user.updatePoint(point.get());
        userRepository.save(user);
    }
    public LocalDateTime changePaidAtLocalDateTime(Date paidAt) {
        Instant instant = paidAt.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
