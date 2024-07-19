package org.example.springaper.domain.payment.repository;

import org.example.springaper.domain.payment.entity.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {
    Optional<PaymentInfo> findByMerchantUid(String merchantUid);

    @Query("SELECT pi FROM PaymentInfo pi " +
            "JOIN FETCH pi.orders o " +
            "JOIN FETCH o.user u " +
            "JOIN FETCH o.ordersDetailList od " +
            "JOIN FETCH od.digitalProduct dp " +
            "WHERE pi.merchantUid = :merchantUid " +
            "AND pi.orders.ordersId = o.ordersId " +
            "AND o.user.userId = u.userId " +
            "AND o.ordersId = od.orders.ordersId " +
            "AND od.digitalProduct.productId = dp.productId")
    Optional<PaymentInfo> findPaymentInfoWithDetailsByMerchantUid(String merchantUid);

    @Query("SELECT pi FROM PaymentInfo pi " +
            "JOIN FETCH pi.orders o " +
            "JOIN FETCH o.user u " +
            "JOIN FETCH o.ordersDetailList od " +
            "JOIN FETCH od.digitalProduct dp " +
            "WHERE pi.orders.ordersId = :ordersId " +
            "AND pi.orders.ordersId = o.ordersId " +
            "AND o.user.userId = u.userId " +
            "AND o.ordersId = od.orders.ordersId " +
            "AND od.digitalProduct.productId = dp.productId")
    Optional<PaymentInfo> findPaymentInfoWithDetailsByOrdersId(Long ordersId);
}
