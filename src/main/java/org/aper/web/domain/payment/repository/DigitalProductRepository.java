package org.aper.web.domain.payment.repository;


import com.aperlibrary.payment.entity.DigitalProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DigitalProductRepository extends JpaRepository<DigitalProduct, Long> {
}
