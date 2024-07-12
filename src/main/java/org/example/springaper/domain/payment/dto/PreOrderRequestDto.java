package org.example.springaper.domain.payment.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PreOrderRequestDto {
    private String paymentMethod;
    private String impUid;
    private String paymentAmount;
    private List<Integer> orderItems;
}
