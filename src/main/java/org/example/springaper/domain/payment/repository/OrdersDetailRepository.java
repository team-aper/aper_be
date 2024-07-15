package org.example.springaper.domain.payment.repository;

import org.example.springaper.domain.payment.entity.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Long> {
    List<OrdersDetail> findAllByOrdersId(Long ordersId);
}
