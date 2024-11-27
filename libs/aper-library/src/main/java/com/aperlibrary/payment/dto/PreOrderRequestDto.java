package com.aperlibrary.payment.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PreOrderRequestDto {
    private String merchantUid;
    private String paymentMethod;
    private List<Integer> orderItems;
}
