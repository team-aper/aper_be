package org.example.springaper.domain.payment.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class PreOrderRequestDto {
    private String merchantUid;
    private String paymentMethod;
    private List<Integer> orderItems;
}
