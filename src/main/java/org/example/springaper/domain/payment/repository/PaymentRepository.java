package org.example.springaper.domain.payment.repository;

import org.example.springaper.domain.payment.entity.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentInfo, Long> {
    Optional<PaymentInfo> findByMerchantUid(String merchantUid);
}
