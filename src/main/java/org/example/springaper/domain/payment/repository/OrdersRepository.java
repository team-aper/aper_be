package org.example.springaper.domain.payment.repository;

import org.example.springaper.domain.payment.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
}
