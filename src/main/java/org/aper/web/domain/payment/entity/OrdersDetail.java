package org.aper.web.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aper.web.domain.common.constant.PaymentStatusEnum;
import org.aper.web.domain.common.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "orders_detail")
@NoArgsConstructor
public class OrdersDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ordersdetail_id")
    private Long ordersDetailId;

    @Column
    private Long amount;

    @Column(name = "payment_status")
    private String paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private DigitalProduct digitalProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "cancle_date")
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

    public void updatePaymentStatusRefunded() {
        this.paymentStatus = "refunded";
    }
}
