package com.shoesshop.backend.repository;

import com.shoesshop.backend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findAllByUserId(int userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update address set selected =:selected where user_id =:userId", nativeQuery = true)
    void updateSelectedForUserId(@Param("userId")int userId,@Param("selected") boolean selected);
}
