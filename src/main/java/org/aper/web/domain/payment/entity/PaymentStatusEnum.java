package org.aper.web.domain.payment.entity;

public enum PaymentStatusEnum {
    PENDING("pending"),
    COMPLETED("completed"),
    REFUNDED("refunded");
    private final String paymentStatus;
    PaymentStatusEnum(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public String getPaymentStatus() {
        return this.paymentStatus;
    }
}
