package org.example.springaper.domain.payment.repository;

import org.example.springaper.domain.payment.entity.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {
    Optional<PaymentInfo> findByMerchantUid(String merchantUid);

    @Query("SELECT pi FROM PaymentInfo pi " +
            "JOIN pi.orders o " +
            "WHERE pi.merchantUid = :merchantUid AND o.ordersId = pi.orders.ordersId")
    Optional<PaymentInfo> findPaymentInfoAndOrders(String merchantUid);
}
