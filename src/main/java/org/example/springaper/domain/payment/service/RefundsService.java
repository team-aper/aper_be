package org.example.springaper.domain.payment.service;

import com.google.gson.JsonObject;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.example.springaper.domain.payment.entity.OrdersDetail;
import org.example.springaper.domain.user.entity.User;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class RefundsService {
    private final IamportClient iamportClient;
    public void refundOrder(Long ordersId, User user) {

    }

    public void postRefundsOrder(String impUid) throws IamportResponseException, IOException {
        CancelData cancelData = new CancelData(impUid, true);
        IamportResponse<Payment> iamportResponse = iamportClient.cancelPaymentByImpUid(cancelData);
        if (iamportResponse.getCode() != 0) {
            throw new IllegalArgumentException(iamportResponse.getMessage());
        }
    }
    public void checkValidRefunds(List<OrdersDetail> ordersDetailList, User user) {
        AtomicReference<Long> point = new AtomicReference<>(0L);
        ordersDetailList.stream()
                .forEach(ordersDetail -> {
                    point.updateAndGet(v -> v + ordersDetail.getDigitalProduct().getValue());
                });
        if (user.getPoint() < point.get()) {
            throw new IllegalArgumentException("보유한 포인트가 결제 취소할 포인트 보다 적습니다.");
        }
    }
}
