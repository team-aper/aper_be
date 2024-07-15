package org.example.springaper.domain.payment.repository;

import org.example.springaper.domain.payment.entity.OrdersDetail;
import org.hibernate.query.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Long> {
    List<OrdersDetail> findAllByOrdersOrdersId(Long ordersId);
    Optional<OrdersDetail> findByDigitalProductProductId(Long productId);
}
