package com.happypill.application.repository.order;

import com.happypill.application.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.paymentUid = :paymentUid")
    Optional<Order> findByPaymentUid(@Param("paymentUid") String paymentUid);
}
