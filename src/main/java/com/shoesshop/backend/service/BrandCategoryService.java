package com.shoesshop.backend.service;

import com.shoesshop.backend.entity.BrandCategory;
import com.shoesshop.backend.exception.NotFoundException;
import com.shoesshop.backend.repository.BrandCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class BrandCategoryService {
    private final BrandCategoryRepository brandCategoryRepository;

    public BrandCategory add(BrandCategory brandCategory) {
        return brandCategoryRepository.save(brandCategory);
    }

    public List<BrandCategory> add(List<BrandCategory> listBrand) {
        return brandCategoryRepository.saveAll(listBrand);
    }

    public Map<String, Object> getAll() {
        Map<String, Object> result = new LinkedHashMap<>();
        List<BrandCategory> categories = brandCategoryRepository.findAll();
        int length = categories.size();
        result.put("length", length);
        result.put("data", categories);
        return result;
    }

    public BrandCategory update(int id, BrandCategory brandCategory) {
        if (!brandCategoryRepository.existsById(id)) {
            throw new NotFoundException("Current brand doesn't exist with id: " + id);
        }
        brandCategory.setId(id);
        return brandCategoryRepository.save(brandCategory);
    }

//    @Transactional
    public Map<String, Object> get(int id) {
        BrandCategory brandCategory = brandCategoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Current brand doesn't exists with id: " + id));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", brandCategory);
        return result;
    }

    public void delete(int id) {
        if (!brandCategoryRepository.existsById(id)) {
            throw new NotFoundException("Current brand doesn't not exists with id: " + id);
        }
        brandCategoryRepository.deleteById(id);
    }
}
