package org.aper.web.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "digital_product")
public class DigitalProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    @Column
    private String name;
    @Column
    private Long amount;
    @Column
    private Long value;
}
