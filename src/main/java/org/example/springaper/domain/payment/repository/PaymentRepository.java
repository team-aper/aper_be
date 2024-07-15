package org.example.springaper.domain.payment.repository;

import org.example.springaper.domain.payment.entity.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentInfo, Long> {
}
