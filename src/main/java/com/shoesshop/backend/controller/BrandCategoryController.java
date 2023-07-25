package com.shoesshop.backend.controller;

import com.shoesshop.backend.entity.BrandCategory;
import com.shoesshop.backend.service.BrandCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/brand-category")
@PreAuthorize("hasAnyRole('ANONYMOUS', 'ADMIN', 'USER')")
public class BrandCategoryController {

    private final BrandCategoryService brandCategoryService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBranch() {
        Map<String, Object> response = new LinkedHashMap<>();
        response = brandCategoryService.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('admin:create')")
    public ResponseEntity<BrandCategory> addBranch(@RequestBody BrandCategory brandCategory) {
        return new ResponseEntity<>(brandCategoryService.add(brandCategory), HttpStatus.CREATED);
    }

    @PostMapping("/add-all")
    @PreAuthorize(value = "hasAuthority('admin:create')")
    public ResponseEntity<List<BrandCategory>> addAllBrand(@RequestBody List<BrandCategory> listBrand) {
        return new ResponseEntity<>(brandCategoryService.add(listBrand), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('admin:update')")
    public ResponseEntity<BrandCategory> updateBranch(@PathVariable int id, @RequestBody BrandCategory brandCategory) {
        return ResponseEntity.ok(brandCategoryService.update(id, brandCategory));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBrand(@PathVariable int id) {
        return ResponseEntity.ok(brandCategoryService.get(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteBrand(@PathVariable int id) {
        brandCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
