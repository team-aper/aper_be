package org.aper.web.domain.payment.repository;

import org.aper.web.domain.payment.entity.DigitalProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DigitalProductRepository extends JpaRepository<DigitalProduct, Long> {
}
