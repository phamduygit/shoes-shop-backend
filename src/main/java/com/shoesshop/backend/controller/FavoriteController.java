package com.shoesshop.backend.controller;

import com.shoesshop.backend.dto.FavoriteRequest;
import com.shoesshop.backend.service.FavoriteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/favorite")
@PreAuthorize("hasRole('USER')")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<Map<String, Object>> getFavorite(@RequestParam(required = false, defaultValue = "0") int page,
                                                           @RequestParam(required = false, defaultValue = "8") int pageSize) {
        return ResponseEntity.ok(favoriteService.getAllFavoriteByUserId(page, pageSize));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:create')")
    public ResponseEntity<Map<String, Object>> addFavorite(@RequestBody FavoriteRequest favoriteRequest) {
        Map<String, Object> favorite = favoriteService.addFavorite(favoriteRequest.getShoesId());
        return new ResponseEntity<>(favorite, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<Void> removeFavorite(@PathVariable int id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }

}
