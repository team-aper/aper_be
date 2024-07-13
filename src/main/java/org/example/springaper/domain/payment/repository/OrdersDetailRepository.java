package org.example.springaper.domain.payment.repository;

import org.example.springaper.domain.payment.entity.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Long> {
}
