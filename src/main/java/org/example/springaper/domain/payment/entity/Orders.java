package org.example.springaper.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.springaper.domain.user.entity.User;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

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
    private Payment payment;

    @Column(name = "orders_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime ordersDate;

    public Orders (Long totalAmount, User user, Payment payment) {
        this.totalAmount = totalAmount;
        this.user = user;
        this.payment = payment;
    }
}
