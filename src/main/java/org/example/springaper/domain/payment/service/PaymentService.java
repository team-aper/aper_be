package org.example.springaper.domain.payment.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Prepare;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springaper.domain.payment.dto.PreOrderRequestDto;
import org.example.springaper.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "결제 서비스")
public class PaymentService {
    private final IamportClient iamportClient;
    public void prepareOrder(PreOrderRequestDto preOrderRequestDto, User user) throws IamportResponseException, IOException {
        PrepareData prepareData = new PrepareData(preOrderRequestDto.getMerchantUid(), preOrderRequestDto.getTotalAmount());
        IamportResponse<Prepare> iamportResponse = iamportClient.postPrepare(prepareData);
        if (iamportResponse.getCode() != 0) {
            throw new IllegalArgumentException("사전 결제 실패");
        }
    }
}
