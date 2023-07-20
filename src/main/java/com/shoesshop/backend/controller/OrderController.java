package com.shoesshop.backend.controller;

import com.shoesshop.backend.dto.OrderRequest;
import com.shoesshop.backend.dto.OrderResponse;
import com.shoesshop.backend.dto.RatingRequest;
import com.shoesshop.backend.entity.Order;
import com.shoesshop.backend.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@PreAuthorize(value = "hasAnyAuthority('ADMIN, USER')")
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize(value = "hasAuthority('user:read')")
    public ResponseEntity<Map<String, Object>> getOrderForUser(@RequestParam(defaultValue = "false") boolean isCompleted,
                                                               @RequestParam(required = false, defaultValue = "0") int pageNumber,
                                                               @RequestParam(required = false, defaultValue = "8") int pageSize) {
        return ResponseEntity.ok(orderService.getNotCompleteOrderList(isCompleted, pageNumber, pageSize));
    }

    @GetMapping("/admin")
    @PreAuthorize(value = "hasAuthority('admin:read')")
    public ResponseEntity<Map<String, Object>> getOrderList() {
        return ResponseEntity.ok(orderService.getOrderList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable int id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('user:create')")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.createOrder(orderRequest), HttpStatus.CREATED);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize(value = "hasAuthority('admin:update')")
    public ResponseEntity<OrderResponse> updateShippingStatus(@PathVariable int id, @RequestBody Map<String, Object> shippingStatus) {
        Order.ShippingStatus status = Order.ShippingStatus.valueOf(shippingStatus.get("shippingStatus").toString());
        return ResponseEntity.ok(orderService.updateShippingStatus(id, status));
    }

    @PutMapping("/{orderId}")
    @PreAuthorize(value = "hasAuthority('user:update')")
    public ResponseEntity<OrderResponse> addRatingAndComment(@PathVariable int orderId, @RequestBody RatingRequest ratingRequest) {
        return ResponseEntity.ok(orderService.addRatingAndComment(orderId, ratingRequest));
    }
}
