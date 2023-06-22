package com.shoesshop.backend.repository;

import com.shoesshop.backend.entity.Shoes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoesRepository extends JpaRepository<Shoes, Integer> {
    Page<Shoes> findByNameContainingOrderByPriceAsc(String name, Pageable paging);
    Page<Shoes> findByNameContainingOrderByPriceDesc(String name, Pageable paging);
    Page<Shoes> findByNameContainingOrderByCreatedAtDesc(String name, Pageable paging);
    Page<Shoes> findByNameContaining(String name, Pageable paging);
    Page<Shoes> findAllByOrderByPriceAsc(Pageable paging);
    Page<Shoes> findAllByOrderByPriceDesc(Pageable paging);
    Page<Shoes> findAllByOrderByCreatedAtDesc(Pageable paging);
    Page<Shoes> findAll(Pageable paging);
}
