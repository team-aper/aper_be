package org.example.springaper.domain.payment.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.springaper.domain.payment.entity.OrdersDetail;
import org.example.springaper.domain.payment.entity.PaymentInfo;
import org.example.springaper.domain.payment.repository.PaymentInfoRepository;
import org.example.springaper.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Transactional
public class RefundsService {
    private final IamportClient iamportClient;
    private final PaymentInfoRepository paymentInfoRepository;
    public void refundsOrder(Long ordersId, User user) throws IamportResponseException, IOException {
        PaymentInfo paymentInfo = paymentInfoRepository.findPaymentInfoWithDetailsByOrdersId(ordersId).orElseThrow(() ->
                new IllegalArgumentException("존재 하지 않는 주문 내용입니다.")
        );
        Long userPoint = getOrdersPoint(paymentInfo.getOrders().getOrdersDetailList());
        checkValidRefunds(user, userPoint);
        postRefundsOrder(paymentInfo.getImpUid());
        updateRefundsPaymentInfo(paymentInfo, userPoint);
    }
    public void postRefundsOrder(String impUid) throws IamportResponseException, IOException {
        CancelData cancelData = new CancelData(impUid, true);
        IamportResponse<Payment> iamportResponse = iamportClient.cancelPaymentByImpUid(cancelData);
        if (iamportResponse.getCode() != 0) {
            throw new IllegalArgumentException(iamportResponse.getMessage());
        }
    }
    public void checkValidRefunds(User user, Long point) {
        if (user.getPoint() < point) {
            throw new IllegalArgumentException("보유한 포인트가 결제 취소할 포인트 보다 적습니다.");
        }
    }
    public Long getOrdersPoint(List<OrdersDetail> ordersDetailList) {
        AtomicReference<Long> point = new AtomicReference<>(0L);
        ordersDetailList.stream()
                .forEach(ordersDetail -> {
                    point.updateAndGet(v -> v + ordersDetail.getDigitalProduct().getValue());
                });
        return point.get();
    }
    public void updateRefundsPaymentInfo(PaymentInfo paymentInfo, Long refundPoint) {
        List<OrdersDetail> ordersDetailList = paymentInfo.getOrders().getOrdersDetailList();
        ordersDetailList.stream().
                forEach(ordersDetail -> {
                    ordersDetail.updatePaymentStatusRefunded();
                    ordersDetail.updateCancleDate(LocalDateTime.now());
                });
        paymentInfo.getOrders().getUser().updatePoint(-refundPoint);
    }
}
