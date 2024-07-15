package org.example.springaper.domain.payment.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "orders_detail")
public class OrdersDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ordersdetail_id")
    private Long ordersDetailId;
    @Column
    private Long amount;
    @Column(name = "payment_status")
    private String paymentStatus;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private DigitalProduct digitalProduct;
    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders orders;
    @Column
    @Nullable
    private LocalDateTime paymentDate;
    @Column
    @Nullable
    private LocalDateTime cancleDate;

    public OrdersDetail(DigitalProduct digitalProduct, Orders orders) {
        this.amount = digitalProduct.getAmount();
        this.digitalProduct = digitalProduct;
        this.orders = orders;
        this.paymentStatus = PaymentStatusEnum.PENDING.getPaymentStatus();
    }

    public void updatePaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
    public void updateCancleDate(LocalDateTime cancleDate) {
        this.cancleDate = cancleDate;
    }
    public void updatePaymentStatusPaid() {
        this.paymentStatus = "paid";
    }
}
