package com.shoesshop.backend.respository;

import com.shoesshop.backend.entity.Shoes;
import org.springframework.data.repository.CrudRepository;

public interface ShoesRepository extends CrudRepository<Shoes, Integer> {
    
}
