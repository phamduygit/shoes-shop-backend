package com.shoesshop.backend.repository;

import com.shoesshop.backend.entity.Shoes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShoesRepository extends JpaRepository<Shoes, Integer> {
    @Query(value =
            "SELECT * FROM shoes\n" +
                    "WHERE MATCH (name) AGAINST (?1) OR name LIKE %?1%",
            nativeQuery = true,
            countQuery = "SELECT * FROM shoes\n" +
                    "WHERE MATCH (name) AGAINST (?1) OR name LIKE %?1%")
    Page<Shoes> findByName(String name, Pageable pageable);

    @Query(value = """
            select * from shoes
            where brands_id = ?1\s
            """,
            nativeQuery = true)
    Page<Shoes> findByBrandId(int brandId, Pageable paging);

    @Query(value =
            "SELECT * FROM shoes\n" +
                    "WHERE (MATCH (name) AGAINST (?1) OR name LIKE %?1%) AND brands_id = ?2" ,
            nativeQuery = true,
            countQuery = "SELECT * FROM shoes\n" +
                    "WHERE (MATCH (name) AGAINST (?1) OR name LIKE %?1%) AND brands_id = ?2")
    Page<Shoes> findByNameAndBrandId(String name, int brandId, Pageable pageable);

}
