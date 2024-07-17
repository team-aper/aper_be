package org.example.springaper.domain.payment.service;

import com.google.gson.JsonObject;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.example.springaper.domain.user.entity.User;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
}
