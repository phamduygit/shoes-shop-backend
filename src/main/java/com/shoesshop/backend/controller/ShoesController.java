package com.shoesshop.backend.controller;

import com.shoesshop.backend.dto.ShoesDetailResponse;
import com.shoesshop.backend.dto.ShoesRequest;
import com.shoesshop.backend.entity.Shoes;
import com.shoesshop.backend.service.ShoesService;
import jakarta.validation.Valid;

import java.util.*;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/shoes")
@PreAuthorize("hasAnyRole('ANONYMOUS', 'ADMIN', 'USER')")
public class ShoesController {

    private final ShoesService shoesService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Map<String, Object>> saveShoes(@Valid @RequestBody Shoes shoes, @RequestParam int brandId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> otherResult = new HashMap<>();
        result.put("data", shoesService.createNewShoes(shoes, brandId));
        otherResult.put("data", result);
        return new ResponseEntity<>(otherResult, HttpStatus.CREATED);
    }

    @PostMapping("/create-all")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Map<String, Object>> addListShoes(@RequestBody List<ShoesRequest> listShoesRequest) {
        List<Shoes> listShoes = new ArrayList<>();
        Map<String, Object> responseResult = new LinkedHashMap<>();
        listShoes = shoesService.addListShoes(listShoesRequest);
        responseResult.put("length", listShoes.size());
        responseResult.put("data", listShoes);
        return new ResponseEntity<>(responseResult, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllShoes(@RequestParam(required = false, defaultValue = "") String name,
                                                           @RequestParam(required = false) String price,
                                                           @RequestParam(required = false) String newest,
                                                           @RequestParam(required = false, defaultValue = "0") int brandId,
                                                           @RequestParam(required = false, defaultValue = "0") int page,
                                                           @RequestParam(required = false, defaultValue = "8") int pageSize) {

        Map<String, Object> filterdListShoes = new LinkedHashMap<>();
        if (price != null) {
            filterdListShoes = shoesService.getAllShoes(name, Integer.parseInt(price), false, brandId, page, pageSize);
        } else if (newest != null) {
            filterdListShoes = shoesService.getAllShoes(name, 0, true, brandId, page, pageSize);
        } else {
            filterdListShoes = shoesService.getAllShoes(name, 0, false, brandId, page, pageSize);
        }
        return new ResponseEntity<>(filterdListShoes, HttpStatus.OK);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<Shoes>> findAllShoes() {
        return new ResponseEntity<>(shoesService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoesDetailResponse> getShoes(@PathVariable int id) {
        ShoesDetailResponse shoes = shoesService.getShoes(id);
        return new ResponseEntity<>(shoes, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllShoesByBrandId(@RequestParam int brandId,
                                                                    @RequestParam(required = false, defaultValue = "0") int page,
                                                                    @RequestParam(required = false, defaultValue = "8") int pageSize) {
        return ResponseEntity.ok(shoesService.getAllShoesByBrandId(brandId, page, pageSize));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<Shoes> updateUserById(@PathVariable int id, @Valid @RequestBody Shoes shoes, @RequestParam int brandId) {
        Shoes updatedShoes = shoesService.updateUserById(id, shoes, brandId);
        if (updatedShoes == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedShoes);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteShoes(@PathVariable int id) {
        boolean deleted = shoesService.deleteShoesById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
