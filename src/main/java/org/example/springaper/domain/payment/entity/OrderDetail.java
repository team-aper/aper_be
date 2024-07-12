package org.example.springaper.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderdetail_id")
    private Long orderDetailId;
    @Column
    private Long amount;
    @Column(name = "payment_status")
    private String paymentStatus;
    @OneToOne(mappedBy = "productId")
    private DigitalProduct digitalProduct;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
