package org.example.springaper.domain.payment.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Prepare;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springaper.domain.payment.dto.PreOrderRequestDto;
import org.example.springaper.domain.payment.entity.DigitalProduct;
import org.example.springaper.domain.payment.entity.Orders;
import org.example.springaper.domain.payment.entity.OrdersDetail;
import org.example.springaper.domain.payment.repository.DigitalProductRepository;
import org.example.springaper.domain.payment.repository.OrdersDetailRepository;
import org.example.springaper.domain.payment.repository.OrdersRepository;
import org.example.springaper.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "결제 서비스")
public class PaymentService {
    private final IamportClient iamportClient;
    private final OrdersRepository ordersRepository;
    private final OrdersDetailRepository ordersDetailRepository;
    private final DigitalProductRepository digitalProductRepository;
    public void prepareOrder(PreOrderRequestDto preOrderRequestDto, User user) throws IamportResponseException, IOException {
        PrepareData prepareData = new PrepareData(preOrderRequestDto.getMerchantUid(), preOrderRequestDto.getTotalAmount());
        IamportResponse<Prepare> iamportResponse = iamportClient.postPrepare(prepareData);
        Orders orders = new Orders(preOrderRequestDto.getTotalAmount().longValue(), user, preOrderRequestDto);
        preOrderRequestDto.getOrderItems().stream()
                .forEach(productId -> {
                    DigitalProduct digitalProduct = digitalProductRepository.findById(productId.longValue()).orElseThrow(
                            () -> new IllegalArgumentException("해당 상품이 없습니다.")
                    );
                    OrdersDetail ordersDetail = new OrdersDetail(digitalProduct, orders);
                });
        if (iamportResponse.getCode() != 0) {
            throw new IllegalArgumentException("사전 결제 실패");
        }
    }
}
