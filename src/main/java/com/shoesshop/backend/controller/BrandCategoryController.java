package com.shoesshop.backend.controller;

import com.shoesshop.backend.entity.BrandCategory;
import com.shoesshop.backend.service.BrandCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/brand-category")
public class BrandCategoryController {

    private final BrandCategoryService brandCategoryService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBranch() {
        return ResponseEntity.ok(brandCategoryService.getAll());
    }

    @PostMapping
    public ResponseEntity<BrandCategory> addBranch(@RequestBody BrandCategory brandCategory) {
        return new ResponseEntity<>(brandCategoryService.add(brandCategory), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandCategory> updateBranch(@PathVariable int id, @RequestBody BrandCategory brandCategory) {
        return ResponseEntity.ok(brandCategoryService.update(id, brandCategory));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBrand(@PathVariable int id) {
        return ResponseEntity.ok(brandCategoryService.get(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable int id) {
        brandCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
