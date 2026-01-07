package org.aper.web.domain.payment.repository;


import org.aper.web.entity.payment.entity.DigitalProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DigitalProductRepository extends JpaRepository<DigitalProduct, Long> {
}
