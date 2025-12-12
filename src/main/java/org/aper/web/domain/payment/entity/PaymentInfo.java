package org.aper.web.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.aper.web.domain.common.entity.BaseEntity;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "payment_info")
@NoArgsConstructor
public class PaymentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentinfo_id")
    private Long paymentinfoId;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "imp_uid")
    private String impUid;

    @Column(name = "merchant_uid")
    private String merchantUid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    // DB에 추가될때가 아닌 아임포트에서 결제된 시간 기입
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    public PaymentInfo(String merchantUid) {
        this.merchantUid = merchantUid;
    }

    public void updateImpUid(String impUid) {
        this.impUid = impUid;
    }

    public void updatePaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void updateOrders(Orders orders) {
        this.orders = orders;
    }

    public void updatePaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
