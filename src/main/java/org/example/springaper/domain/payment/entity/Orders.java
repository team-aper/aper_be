package org.example.springaper.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.springaper.domain.user.entity.User;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long ordersId;

    @Column
    private Long totalAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private PaymentInfo paymentInfo;

    //주문의 생성 날짜, 실제 결제 날짜 x
    @Column(name = "orders_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime ordersDate;

    public Orders (Long totalAmount, User user, PaymentInfo paymentInfo) {
        this.totalAmount = totalAmount;
        this.user = user;
        this.paymentInfo = paymentInfo;
        this.ordersDate = LocalDateTime.now();
    }
}
