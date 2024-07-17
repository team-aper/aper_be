package org.example.springaper.domain.payment.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PreOrderResponseDto {
    private BigDecimal amount;
    private String paymentTitle;

    public PreOrderResponseDto(BigDecimal amount, String paymentTitle) {
        this.amount = amount;
        this.paymentTitle = paymentTitle;
    }
}
