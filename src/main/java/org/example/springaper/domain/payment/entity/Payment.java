package org.example.springaper.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long paymentId;

    @Column
    private String paymentMethod;

    @Column
    private String impUid;

    @Column
    private String merchantUid;

    //DB에 추가될때까 아닌 아임포트에서 결제된 시간 기입
    @Column
    private LocalDateTime paymentDate;
}
