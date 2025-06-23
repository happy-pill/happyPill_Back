package com.happypill.application.repository.subscription;

import com.happypill.application.entity.Subscription;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Query("""
            SELECT pi.name, u.notifyEmail, s.id, s.nextDeliveryDate
            FROM Subscription s
            JOIN s.orderLine ol
            JOIN s.user u
            JOIN ol.order o
            JOIN ol.product p
            JOIN ProductInfo pi ON pi.product = p AND pi.language = :language
            WHERE s.isCompleted = :isCompleted
            AND o.status = :orderStatus
            ORDER BY s.id DESC
            """)
    Page<Object[]> getActiveSubscriptionsByLanguage(Language language, boolean isCompleted, OrderStatus orderStatus, Pageable pageable);
}