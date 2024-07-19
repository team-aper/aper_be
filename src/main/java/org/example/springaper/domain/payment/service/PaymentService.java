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
import org.example.springaper.domain.payment.dto.PreOrderResponseDto;
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
import org.hibernate.query.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "결제 서비스")
@Transactional
public class PaymentService {
    private final IamportClient iamportClient;
    private final OrdersRepository ordersRepository;
    private final OrdersDetailRepository ordersDetailRepository;
    private final DigitalProductRepository digitalProductRepository;
    private final PaymentInfoRepository paymentInfoRepository;
    private final UserRepository userRepository;
    public PreOrderResponseDto prepareOrder(PreOrderRequestDto preOrderRequestDto, User user) throws IamportResponseException, IOException {
        List<DigitalProduct> orderedProducts = getProductList(preOrderRequestDto.getOrderItems());
        if (preOrderRequestDto.getOrderItems().size() != orderedProducts.size()) {
            throw new IllegalArgumentException("존재하지 않는 상품에 대한 주문입니다.");
        }

        BigDecimal totalAmount = orderedProducts.stream()
                .map(product -> new BigDecimal(product.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        PrepareData prepareData = new PrepareData(preOrderRequestDto.getMerchantUid(), totalAmount);
        IamportResponse<Prepare> iamportResponse = iamportClient.postPrepare(prepareData);
        if (iamportResponse.getCode() != 0) {
            throw new IllegalArgumentException(iamportResponse.getMessage());
        }
        log.info("사전 결제 아임포트 추가 성공");

        PaymentInfo prePaymentInfo = new PaymentInfo(preOrderRequestDto);
        paymentInfoRepository.save(prePaymentInfo);


        Orders preOrders = createOrder(totalAmount.longValue(), user, prePaymentInfo);
        log.info("사전 주문 테이블 생성 성공");

        createOrdersDetail(preOrders, orderedProducts);
        return new PreOrderResponseDto(totalAmount, "포인트 구매");
    }
    public void postOrder(String impUid, User user) throws IamportResponseException, IOException {
        IamportResponse<Payment> payment = iamportClient.paymentByImpUid(impUid);
        Payment response = payment.getResponse();
        LocalDateTime responsePaidAt = changePaidAtLocalDateTime(response.getPaidAt());

        PaymentInfo paymentInfo = paymentInfoRepository.findPaymentInfoWithDetailsByMerchantUid(response.getMerchantUid()).orElseThrow(() ->
                new IllegalArgumentException("존재 하지 않는 주문 내용입니다.")
        );

        Orders orders = paymentInfo.getOrders();

        if (!Objects.equals(orders.getUser().getUserId(), user.getUserId())) {
            throw new IllegalArgumentException("주문한 유저와 결제한 유저가 일치하지 않습니다.");
        }

        List<OrdersDetail> ordersDetailList = orders.getOrdersDetailList();

        paymentInfo.updateImpUid(response.getImpUid());
        paymentInfo.updatePaymentDate(responsePaidAt);
        paymentInfo.updatePaymentMethod(response.getPayMethod());

        user.updatePoint(processPaymentAndUpdatePoint(responsePaidAt, ordersDetailList));
        userRepository.save(user);
    }


    public Long processPaymentAndUpdatePoint(LocalDateTime responsePaidAt, List<OrdersDetail> ordersDetailList) {
        AtomicReference<Long> point = new AtomicReference<>(0L);
        ordersDetailList.stream()
                .forEach(ordersDetail -> {
                    ordersDetail.updatePaymentDate(responsePaidAt);
                    ordersDetail.updatePaymentStatusPaid();
                    point.updateAndGet(v -> v + ordersDetail.getDigitalProduct().getValue());
                });
        return point.get();
    }
    public void createOrdersDetail(Orders orders, List<DigitalProduct> productList) {
        List<OrdersDetail> ordersDetails = productList.stream()
                .map(product -> new OrdersDetail(product, orders))
                .toList();

        ordersDetailRepository.saveAll(ordersDetails);
        log.info("주문 디테일 생성 성공");
    }
    public LocalDateTime changePaidAtLocalDateTime(Date paidAt) {
        Instant instant = paidAt.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
    public List<DigitalProduct> getProductList(List<Integer> productList) {
        List<Long> productIds = productList.stream()
                .map(Long::valueOf)
                .toList();
        return digitalProductRepository.findAllById(productIds);
    }
    public Orders createOrder(Long totalAmount, User user, PaymentInfo prePaymentInfo) {
        Orders preOrders = new Orders(totalAmount, user, prePaymentInfo);
        prePaymentInfo.updateOrders(preOrders);
        return ordersRepository.save(preOrders);
    }
}