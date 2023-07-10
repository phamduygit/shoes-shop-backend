package com.shoesshop.backend.controller;

import com.shoesshop.backend.dto.CartItemRequest;
import com.shoesshop.backend.dto.CartItemResponse;
import com.shoesshop.backend.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@PreAuthorize(value = "hasAuthority('USER'))")
@RequestMapping(value = "/api/v1/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping
    @PreAuthorize(value = "hasAuthority('user:read')")
    public ResponseEntity<Map<String, Object>> getCart(@RequestParam(required = false, defaultValue = "0") int pageNumber,
                                                       @RequestParam(required = false, defaultValue = "8") int pageSize) {
        return ResponseEntity.ok(cartService.getCart(pageNumber, pageSize));
    }

    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('user:read')")
    public ResponseEntity<CartItemResponse> getCartDetail(@PathVariable int id) {
        return ResponseEntity.ok(cartService.getCartDetail(id));
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('user:create')")
    public ResponseEntity<CartItemResponse> addItemToCart(@RequestBody CartItemRequest cartItemRequest) {
        return new ResponseEntity<>(cartService.addCartItem(cartItemRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('user:update')")
    public ResponseEntity<CartItemResponse> updateItemInCart(@PathVariable int id, @RequestBody CartItemRequest cartItemRequest) {
        return ResponseEntity.ok(cartService.updateCartItem(cartItemRequest, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('user:delete')")
    public ResponseEntity<Void> deleteItemInCart(@PathVariable int id) {
        cartService.deleteCartItem(id);
        return ResponseEntity.noContent().build();
    }
}
