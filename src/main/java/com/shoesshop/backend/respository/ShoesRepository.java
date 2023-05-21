package com.shoesshop.backend.respository;

import com.shoesshop.backend.entity.Shoes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoesRepository extends JpaRepository<Shoes, Integer> {
    List<Shoes> findByNameContainingOrderByPriceAsc(String name);
    List<Shoes> findByNameContainingOrderByPriceDesc(String name);
    List<Shoes> findByNameContainingOrderByCreatedAtDesc(String name);
    List<Shoes> findByNameContaining(String name);
    List<Shoes> findAllByOrderByPriceAsc();
    List<Shoes> findAllByOrderByPriceDesc();
    List<Shoes> findAllByOrderByCreatedAtDesc();
}
