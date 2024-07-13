package org.example.springaper.domain.payment.repository;

import org.example.springaper.domain.payment.entity.DigitalProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DigitalProductRepository extends JpaRepository<DigitalProduct, Long> {
}
