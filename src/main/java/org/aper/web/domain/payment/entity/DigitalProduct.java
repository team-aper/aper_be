package org.aper.web.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "digital_product")
@NoArgsConstructor
public class DigitalProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column
    private String name;

    @Column
    private Long amount;

    @Column(name = "value")
    private Long value;
}
