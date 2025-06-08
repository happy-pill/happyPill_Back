package com.happypill.application.repository.order;

import com.happypill.application.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
           SELECT ol.product.productId
           FROM Order o
           JOIN o.orderLines ol
           GROUP BY ol.product.productId
           """)
    List<Long> findTopSellingProductIds();
}
