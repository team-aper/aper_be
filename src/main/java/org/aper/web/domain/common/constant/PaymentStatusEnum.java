package org.aper.web.domain.common.constant;

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
