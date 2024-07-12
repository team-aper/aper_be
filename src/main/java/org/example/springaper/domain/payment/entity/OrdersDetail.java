package org.example.springaper.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;

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
    @OneToOne
    @JoinColumn(name = "product_id")
    private DigitalProduct digitalProduct;
    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders orders;
}
