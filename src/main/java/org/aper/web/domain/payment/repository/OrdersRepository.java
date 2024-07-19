package org.aper.web.domain.payment.repository;

import org.aper.web.domain.payment.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> findByPaymentInfoPaymentinfoId(Long paymentInfoId);
}
