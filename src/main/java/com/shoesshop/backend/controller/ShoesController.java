package com.shoesshop.backend.controller;

import com.shoesshop.backend.entity.Shoes;
import com.shoesshop.backend.service.ShoesService;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/shoes")
public class ShoesController {

    @Autowired
    private ShoesService shoesService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> saveShoes(@Valid @RequestBody Shoes shoes) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> otherResult = new HashMap<>();
        result.put("data", shoesService.createNewShoes(shoes));
        otherResult.put("data", result);
        return new ResponseEntity<>(otherResult, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllShoes() {
        List<Map<String, Object>> listShoes = shoesService.getAllShoes();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("length", listShoes.size());
        result.put("data", listShoes);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shoes> getShoes(@PathVariable int id) {
        Shoes shoes = shoesService.getShoes(id);
        return new ResponseEntity<>(shoes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shoes> updateUserById(@PathVariable int id, @Valid @RequestBody Shoes shoes) {
        Shoes updatedShoes = shoesService.updateUserById(id, shoes);
        if (updatedShoes == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedShoes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoes(@PathVariable int id) {
        boolean deleted = shoesService.deleteShoesById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
