package com.shoesshop.backend.repository;

import com.shoesshop.backend.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findAllByUserIdAndShippingStatus(int userId, Order.ShippingStatus shippingStatus, Pageable pageable);
    @Query(
            value = "SELECT * FROM orders WHERE user_id = ?1 AND shipping_status != ?2",
            countQuery = "SELECT * FROM orders WHERE user_id = ?1 AND shipping_status != ?2",
            nativeQuery = true
    )
    Page<Order> findAllByUserIdAndWithoutShippingStatus(int userId, String shippingStatus, Pageable pageable);
}
