package org.example.springaper.domain.payment.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PreOrderResponseDto {
    private BigDecimal amount;

    public PreOrderResponseDto(BigDecimal amount) {
        this.amount = amount;
    }
}
