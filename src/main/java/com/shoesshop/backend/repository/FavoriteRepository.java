package com.shoesshop.backend.repository;

import com.shoesshop.backend.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    @Query(value = """
            SELECT * FROM favorite favorite
            WHERE user_id = ?1\s
            """,
            countQuery = """
            SELECT * FROM favorite favorite
            WHERE user_id = ?1\s
            """,
            nativeQuery = true)
    Page<Favorite> findAllByUserId(int userId, Pageable pageable);
}
