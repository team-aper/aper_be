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
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long orderId;

    @Column
    private Long totalAmount;

    @OneToMany(mappedBy = "userId")
    private List<User> user;

    @OneToOne(mappedBy = "paymentId")
    private Payment payment;

    @Column(name = "order_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime orderDate;
}
