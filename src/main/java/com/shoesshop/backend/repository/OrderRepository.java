package com.shoesshop.backend.repository;

import com.shoesshop.backend.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findAllByUserIdAndShippingStatus(int userId, Order.ShippingStatus shippingStatus, Pageable pageable);
}
