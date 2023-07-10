package com.shoesshop.backend.repository;

import com.shoesshop.backend.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Page<CartItem> findAllByCartId(int cartId, Pageable pageable);
    @Query(value = "SELECT * FROM cart_item WHERE shoes_id = ?1 AND cart_id = ?2", nativeQuery = true)
    Optional<CartItem> findByShoesIdAndCartId(int shoesId, int cartId);
    List<CartItem> findAllByCartId(int cartId);
}
