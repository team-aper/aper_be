package org.aper.web.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import org.aper.web.domain.common.entity.BaseEntity;
import org.aper.web.domain.common.constant.PaymentStatusEnum;
import org.aper.web.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long ordersId;

    @Column(name = "total_amount")
    private Long totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentinfo_id")
    private PaymentInfo paymentInfo;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrdersDetail> ordersDetailList = new ArrayList<>();

    // 주문의 생성 날짜, 실제 결제 날짜 x
    @Column(name = "orders_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime ordersDate;

    public Orders(Long totalAmount, User user, PaymentInfo paymentInfo) {
        this.totalAmount = totalAmount;
        this.user = user;
        this.paymentInfo = paymentInfo;
        this.ordersDate = LocalDateTime.now();
    }
}
